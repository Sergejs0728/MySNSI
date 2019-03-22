package com.scubasnsi.mysnsi.controllers.logbook.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.scubasnsi.R;
import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.app.listeners.UpdateLogbookHeader;
import com.scubasnsi.mysnsi.app.utilities.GenericDialogs;
import com.scubasnsi.mysnsi.controllers.logbook.OnNextListener;
import com.scubasnsi.mysnsi.model.data_models.Logbook;
import com.scubasnsi.mysnsi.model.sessions.UserSession;

import java.util.List;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import shiva.joshi.common.callbacks.GenericConfirmationDialogBoxCallback;
import shiva.joshi.common.logger.AppLogger;
import shiva.joshi.common.receivers.ConnectivityReceiver;

import static shiva.joshi.common.CommonConstants.BUNDLE_SERIALIZED_OBJECT;


@RuntimePermissions
public class DiveSiteLocationFragment extends Fragment implements OnNextListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = "com.snsi.controllers.logbbook.fragments.DiveSiteLocationFragment";
    public static final float MAP_ZOOM_LEVEL = 10f;
    private Context mContext;
    private UserSession mUserSession;


    @BindString(R.string.dive_location_title)
    protected String mTitle;

    @BindView(R.id.logback_location_map)
    protected MapView mMapView;

    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;

    private Logbook mLogbook;
    private UpdateLogbookHeader mUpdateLogbookHeader;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LatLng mLatLng;
    private Marker mMarker;
    private LocationManager locationManager;


    public static DiveSiteLocationFragment newInstance(Logbook logbook) {
        DiveSiteLocationFragment fragment = new DiveSiteLocationFragment();
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_SERIALIZED_OBJECT, logbook);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mUpdateLogbookHeader != null)
            mUpdateLogbookHeader.OnUpdateHeader(true, mTitle);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UpdateLogbookHeader) {
            mUpdateLogbookHeader = (UpdateLogbookHeader) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement UpdateHomeHeader");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mUserSession = MyApplication.getApplicationInstance().getUserSession();
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (TextUtils.isEmpty(getResources().getString(R.string.google_maps_api_key))) {
            throw new IllegalStateException("You forgot to supply a Google Maps API key");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_dive_site_location, container, false);
        ButterKnife.bind(this, view);
        mUpdateLogbookHeader.OnUpdateHeader(true, mTitle);
        if (getArguments() != null) {
            mLogbook = (Logbook) getArguments().getSerializable(BUNDLE_SERIALIZED_OBJECT);
        }

        return view;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Gets the MapView from the XML layout and creates it
        if (mMapView != null) {
            mMapView.onCreate(savedInstanceState);
            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                }
            });
        } else {
            AppLogger.showToastForDebug(TAG, "Error - Map Fragment was null!!");
        }
    }


    // Load googleMap
    protected void loadMap(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (mGoogleMap != null) {
            DiveSiteLocationFragmentPermissionsDispatcher.getMyLocationWithCheck(this);
        } else {
            AppLogger.showToastForDebug(TAG, "Error - Map was null!!");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        DiveSiteLocationFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @SuppressWarnings("all")
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void getMyLocation() {
        if (mGoogleMap != null) {
            // Now that map has loaded, let's get our location!
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            connectClient();
            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    getMarker(latLng).showInfoWindow();
                    //Set clicked lat long
                    setLatLng(latLng);
                }
            });
        }
    }

    // Connect the client.
    protected void connectClient() {
        if (isGooglePlayServicesAvailable() && mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    // Dis-Connect the client
    protected void disConnectClient() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mMapView != null) {
            mMapView.onPause();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
            removeMarker();
            if (mGoogleMap != null)
                loadMap(mGoogleMap);
        }

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapView != null)
            mMapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null)
            mMapView.onDestroy();
        // Disconnecting the client invalidates it.
        disConnectClient();
        mUpdateLogbookHeader = null;
    }

    @Override
    public Logbook next() {
        if (getLatLng() != null) {
            mLogbook.setLatitude(String.valueOf(getLatLng().latitude));
            mLogbook.setLongitude(String.valueOf(getLatLng().longitude));
        }


        //// TODO: 11-05-2017 Removed , as @Gaurav sir said , Locations is not mandatory
       /* if (mLogbook.getLatitude() == 0F && mLogbook.getLongitude() == 0f) {
            GenericDialogs.showInformativeDialog("", R.string.verify_dive_location, mContext);
            return null;
        }*/

        return mLogbook;
    }


    private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {

            GenericDialogs.showInformativeDialog("", R.string.dive_location_play_services_not_available, mContext);

            return false;
        }
    }

    /*
     * Called by Location Services when the request to connect the client
	 * finishes successfully. At this point, you can request the current
	 * location or start periodic updates
	 */

    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        try {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                AppLogger.showToast(TAG, "Please accept permissions");
                return;
            }
            if (mLogbook.getLongitude() != 0 && mLogbook.getLatitude() != 0) {
                LatLng center = new LatLng(mLogbook.getLatitude(), mLogbook.getLongitude());
                // Update Lat lan
                updateMap(center, true);
            } else {
                String locationProvider = LocationManager.GPS_PROVIDER;
                // Or use LocationManager.GPS_PROVIDER

                Location location = locationManager.getLastKnownLocation(locationProvider);
                if (location == null)
                    location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                if (location != null) {

                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    // Update Lat lan
                    updateMap(latLng, false);
                } else {
                    requestForEnablingLocation();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // update Location :
    private void updateMap(LatLng latLng, boolean isShowMarker) {
        if (isShowMarker) {
            getMarker(latLng).showInfoWindow();
            //Set Current lat long
            setLatLng(latLng);
        }

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, MAP_ZOOM_LEVEL);

        mGoogleMap.animateCamera(cameraUpdate);

    }

    /* Request to enable location : */
    private void requestForEnablingLocation() {
        if (!isLocationEnabled()) {
            GenericDialogs.getGenericConfirmDialog(mContext, "Location services", "Please switch on your location services", "Yes", "No", false, new GenericConfirmationDialogBoxCallback() {
                @Override
                public void PositiveMethod(DialogInterface dialog, int id) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }

                @Override
                public void NegativeMethod(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

        }
    }

    /* Check , isLocationEnable : */
    protected boolean isLocationEnabled() {
        String le = Context.LOCATION_SERVICE;
        LocationManager locationManager = (LocationManager) mContext.getSystemService(le);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return false;
        } else {
            return true;
        }
    }

    /*
         * Called by Location Services if the connection to the location client
         * drops because of an error.
         */
    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            AppLogger.showToastForDebug(TAG, "Disconnected. Please re-connect.");
        } else if (i == CAUSE_NETWORK_LOST) {
            AppLogger.showToastForDebug(TAG, "Network lost. Please re-connect.");
        }
    }

    /*
     * Called by Location Services if the attempt to Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult((Activity) mContext,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException ex) {
                AppLogger.Logger.error(TAG, ex.getMessage(), ex);
            }
        } else {
            AppLogger.showToast(TAG, "Sorry. Location services not available to you");
        }
    }


    private LatLng getLatLng() {
        return mLatLng;
    }

    private void setLatLng(LatLng latLng) {
        mLatLng = latLng;
    }


    private Marker getMarker(LatLng latLng) {
        if (mMarker == null) {
            mMarker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Divesite")
            );
        }
        mMarker.setPosition(latLng);
        mMarker.setTitle("Divesite");
        Address address = getAddress(latLng.latitude, latLng.longitude);
        if (address != null) {
            mMarker.setTitle(address.getLocality());
            mMarker.setSnippet(address.getCountryName());
        }

        return mMarker;
    }

    private void removeMarker() {
        if (mMarker != null) {
            mMarker.remove();
            mMarker = null;
        }
    }

    public Address getAddress(double lat, double lng) {
        if (ConnectivityReceiver.isConnected(mContext)) {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                return addresses.get(0);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

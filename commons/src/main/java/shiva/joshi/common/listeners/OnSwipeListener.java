package shiva.joshi.common.listeners;

/*
* This class is a ScrollListener for RecyclerView that allows to show/hide
* views when list is scrolled. It assumes that you have added a header

* */
public interface OnSwipeListener {
    void onSwipeLeft(int position);
    void onSwipeRight(int position);
}
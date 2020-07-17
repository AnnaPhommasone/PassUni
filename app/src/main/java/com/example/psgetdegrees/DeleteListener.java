package com.example.psgetdegrees;

/**
 * An interface for deleting single items from a recycler view.
 */
public interface DeleteListener {

    /**
     * Deletes the item given by the specific ID.
     *
     * @param id the ID of the item to be deleted
     */
    void onClickDel(int id);
}

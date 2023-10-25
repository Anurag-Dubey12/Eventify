package com.example.eventmatics.SwipeGesture

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

abstract class GuestSwipeToDelete(context: Context):
    ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT
        or ItemTouchHelper.RIGHT ) {
    private val  Deleteicon= R.drawable.delete
    private val Deletecolor= ContextCompat.getColor(context,R.color.Light_Pink)

    private val PaidIcon=R.drawable.invitationsent
    private val InvitationSentColor=ContextCompat.getColor(context,R.color.light_blue)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return  false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addSwipeLeftBackgroundColor(Deletecolor)
            .addSwipeRightBackgroundColor(InvitationSentColor)
            .addSwipeLeftActionIcon(Deleteicon)
            .addSwipeRightActionIcon(PaidIcon)
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}

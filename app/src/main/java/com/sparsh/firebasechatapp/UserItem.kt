package com.sparsh.firebasechatapp

import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.signgle_row_user.view.*


class UserItem( val user : User):Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.Name.text = user.username
        Picasso.get().load(user.profileImageUrl).error(R.drawable.grey).into(viewHolder.itemView.UserImage)

    }

    override fun getLayout(): Int {
        return R.layout.signgle_row_user

    }

}

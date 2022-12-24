package com.huaqing.samplerecord.test

import androidx.databinding.BaseObservable

open class GroupModel:ItemExpand,BaseObservable() {
    override var itemPosition: Int=0
    override var itemExpand: Boolean=false
        set(value) {
            field=value
            notifyChange()
        }


}
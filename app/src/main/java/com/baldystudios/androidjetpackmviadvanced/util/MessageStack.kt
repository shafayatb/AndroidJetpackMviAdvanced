package com.baldystudios.androidjetpackmviadvanced.util

import androidx.lifecycle.MutableLiveData
import kotlinx.android.parcel.IgnoredOnParcel

const val MESSAGE_STACK_BUNDLE_KEY = "com.baldystudios.androidjetpackmviadvanced.util.MessageStack"

class MessageStack: ArrayList<StateMessage>() {

    @IgnoredOnParcel
    val stateMessage: MutableLiveData<StateMessage> = MutableLiveData()

    override fun addAll(elements: Collection<StateMessage>): Boolean {
        for(element in elements){
            add(element)
        }
        return true // always return true. We don't care about result bool.
    }

    override fun add(element: StateMessage): Boolean {
        if(this.size == 0){
            setStateMessage(stateMessage = element)
        }
        if(this.contains(element)){ // prevent duplicate errors added to stack
            return false
        }
        return super.add(element)
    }

    override fun removeAt(index: Int): StateMessage {
        try{
            val transaction = super.removeAt(index)
            if(this.size > 0){
                setStateMessage(stateMessage = this[0])
            }
            else{
                setStateMessage(null)
            }
            return transaction
        }catch (e: IndexOutOfBoundsException){
            e.printStackTrace()
        }
        return StateMessage(
            Response(
                message = "does nothing",
                uiComponentType = UIComponentType.None(),
                messageType = MessageType.None()
            )
        ) // this does nothing
    }

    private fun setStateMessage(stateMessage: StateMessage?){
        this.stateMessage.value = stateMessage
    }
}
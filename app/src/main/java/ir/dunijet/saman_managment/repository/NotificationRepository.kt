package ir.dunijet.saman_managment.repository

import ir.dunijet.saman_managment.api.RetrofitInstance
import ir.dunijet.saman_managment.model.PushNotification

class NotificationRepository {
    suspend fun postNotification(notification: PushNotification) = RetrofitInstance.api.postNotification(notification)
}
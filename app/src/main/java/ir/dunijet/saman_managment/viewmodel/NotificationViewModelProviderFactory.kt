package ir.dunijet.saman_managment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ir.dunijet.saman_managment.repository.NotificationRepository

class NotificationViewModelProviderFactory(
    private val notificationRepository: NotificationRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NotificationViewModel(notificationRepository) as T
    }

}
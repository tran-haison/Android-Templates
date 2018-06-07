package nimbl3.com.data.lib.schedulers

import io.reactivex.Scheduler

interface SchedulersProvider {
    fun io(): Scheduler

    fun computation(): Scheduler

    fun main(): Scheduler
}
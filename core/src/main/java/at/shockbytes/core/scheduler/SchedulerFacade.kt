package at.shockbytes.core.scheduler

import io.reactivex.Scheduler

interface SchedulerFacade {

    val ui: Scheduler

    val computation: Scheduler

    val io: Scheduler
}
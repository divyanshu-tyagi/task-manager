package org.example.task_manager.repository

import org.example.task_manager.domain.Priority
import org.example.task_manager.domain.Task
import org.example.task_manager.domain.TaskStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository: JpaRepository<Task, Long> {

    fun findByUserId(userId: Long,pageable: Pageable): Page<Task>

    fun findByUserIdAndStatus(userId: Long, status: TaskStatus,pageable: Pageable): Page<Task>

    fun findByUserIdAndPriority(userId: Long, priority: Priority,pageable: Pageable): Page<Task>

    fun findByUserIdAndStatusAndPriority(
        userId: Long,
        status: TaskStatus,
        priority: Priority,
        pageable: Pageable
    ): Page<Task>

    @Query("SELECT t FROM Task t WHERE t.userId = :userId AND LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%'))")
    fun searchByTitle(userId: Long, search: String, pageable: Pageable): Page<Task>

    @Query("SELECT t FROM Task t WHERE t.userId = :userId AND LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) AND t.status = :status")
    fun searchByTitleAndStatus(userId: Long, search: String, status: TaskStatus, pageable: Pageable): Page<Task>

}
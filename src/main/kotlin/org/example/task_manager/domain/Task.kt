package org.example.task_manager.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(name = "tasks")
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val title: String,

    val description: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: TaskStatus = TaskStatus.TODO,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val priority: Priority = Priority.LOW,

    val dueDate: LocalDate? = null,

    @Column(nullable = false)
    val userId: Long,

    val createdAt: Instant = Instant.now(),
)

enum class TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE
}

enum class Priority{
    LOW,
    MEDIUM,
    HIGH
}

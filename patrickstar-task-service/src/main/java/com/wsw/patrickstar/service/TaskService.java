package com.wsw.patrickstar.service;

import com.wsw.patrickstar.entity.Task;

import java.util.List;

/**
 * @Author WangSongWen
 * @Date: Created in 14:19 2020/11/9
 * @Description:
 */
public interface TaskService {
    Task createTask(Task task);

    Task updateTaskById(Task task);

    Task updateTaskByName(Task task);

    Task updateTaskStatusByTaskId(Task task);

    int deleteTaskByTaskId(Long taskId);

    int deleteTaskByTaskName(String taskName);

    Task selectTaskById(Long taskId);

    List<Task> selectTaskByName(String taskName);

    List<Task> selectTaskByStatus(char taskStatus);

    // ...
}

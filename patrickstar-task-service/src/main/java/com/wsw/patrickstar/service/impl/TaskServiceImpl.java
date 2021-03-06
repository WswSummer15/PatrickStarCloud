package com.wsw.patrickstar.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wsw.patrickstar.entity.Task;
import com.wsw.patrickstar.exception.TaskServiceException;
import com.wsw.patrickstar.feign.client.RecepienterClient;
import com.wsw.patrickstar.mapper.TaskMapper;
import com.wsw.patrickstar.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author WangSongWen
 * @Date: Created in 14:28 2020/11/9
 * @Description: task主服务
 * <p>
 * redis缓存:
 * 1.Cacheable: 将查询结果缓存到redis中,(key="#p0")指定传入的第一个参数作为redis的key
 * 2.CachePut: 指定key,将更新的结果同步到redis中
 * 3.CacheEvict: 指定key,删除缓存数据,(allEntries=true)方法调用后将立即清空所有缓存
 * <p>
 * 同步调用: openFeign
 * 异步调用: RabbitMQ
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "task", cacheManager = "taskCacheManager")
public class TaskServiceImpl implements TaskService {
    @Resource
    private TaskMapper taskMapper;
    @Resource
    private RecepienterClient recepienterClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Task createTask(Task task) throws TaskServiceException {
        // 添加任务
        taskMapper.insert(task);
        // 同步调用
        // 调用recepienter服务添加领取人员信息
        recepienterClient.create(task.getTaskId(), task.getTaskName(), task.getRecepientName(), new Date().toString());
        return task;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CachePut(key = "#task.taskId", unless = "#result == null")
    public Task updateTaskById(Task task) throws TaskServiceException {
        taskMapper.updateById(task);
        return task;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CachePut(key = "#task.taskId", unless = "#result == null")
    public Task updateTaskByName(Task task) throws TaskServiceException {
        UpdateWrapper<Task> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("task_name", task.getTaskName());
        taskMapper.update(task, updateWrapper);
        return task;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CachePut(key = "#task.taskId", unless = "#result == null")
    public Task updateTaskStatusByTaskId(Task task) throws TaskServiceException {
        UpdateWrapper<Task> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .set("task_status", task.getTaskStatus())
                .eq("task_id", task.getTaskId());
        taskMapper.update(task, updateWrapper);
        return task;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = "#p0", allEntries = false)
    public int deleteTaskByTaskId(Long taskId) throws TaskServiceException {
        return taskMapper.deleteById(taskId);
    }

    // 这个方法没有实现数据同步！
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = "#p0", allEntries = false)
    public int deleteTaskByTaskName(String taskName) throws TaskServiceException {
        int result;
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_name", taskName);
        result = taskMapper.delete(queryWrapper);
        return result;
    }

    @Override
    @Cacheable(key = "#p0", unless = "#result == null")
    public Task selectTaskById(Long taskId) throws TaskServiceException {
        return taskMapper.selectById(taskId);
    }

    @Override
    @Cacheable(key = "#p0", unless = "#result == null")
    public List<Task> selectTaskByName(String taskName) throws TaskServiceException {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_name", taskName);
        return taskMapper.selectList(queryWrapper);
    }

    @Override
    @Cacheable(key = "#p0", unless = "#result == null")
    public List<Task> selectTaskByStatus(char taskStatus) throws TaskServiceException {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_status", taskStatus);
        return taskMapper.selectList(queryWrapper);
    }
}

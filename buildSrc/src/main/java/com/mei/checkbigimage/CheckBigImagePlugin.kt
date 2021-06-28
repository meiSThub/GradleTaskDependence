package com.mei.checkbigimage

import com.mei.checkbigimage.task.CheckBigImageTask
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @date 2021/6/28
 * @author mxb
 * @desc   检测大图插件
 * @desired
 */
class CheckBigImagePlugin : Plugin<Project> {
    /**
     * 配置阶段，在调用apply 方法，应用插件的时候，调用此方法
     *
     * @param project
     */
    override fun apply(project: Project) {
        project.afterEvaluate {
            // 查找需要挂接的任务
            val mergeDebugResources = project.tasks.findByName("mergeDebugResources")
            val processDebugResources = project.tasks.findByName("processDebugResources")
            println("mergeResourcesTask=$mergeDebugResources，processDebugResources=$processDebugResources")

            // 创建自己的Task任务
            val checkBigImage = project.tasks.create("checkBigImage", CheckBigImageTask::class.java)
            // 挂接任务
            checkBigImage.mustRunAfter(mergeDebugResources)
            processDebugResources?.dependsOn(checkBigImage)
        }
    }
}
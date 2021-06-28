package com.mei.checkbigimage.task

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * @date 2021/6/28
 * @author mxb
 * @desc 检查大图的任务
 * @desired
 */
open class CheckBigImageTask : DefaultTask() {

    @TaskAction
    fun checkBigImage() {
        val hasAppPlugin = project.plugins.hasPlugin("com.android.application")
        val variants = if (hasAppPlugin) {
            (project.property("android") as AppExtension).applicationVariants
        } else {
            (project.property("library") as LibraryExtension).libraryVariants
        }
        println("variants=${variants}")
        if (variants == null) return

        variants.all { variant ->
            val start = System.currentTimeMillis()
            println("---- 检测大图 ----")
            // 获取全部的原始资源
            val dir: Set<File> = variant.allRawAndroidResources.files
            val imageList = mutableListOf<File>()
            dir.forEach { file ->
                collectBigImage(file, imageList)
                println("文件：${file.absoluteFile}")
            }

            println("收集到的图片：${imageList.size}")
            imageList.forEach {
                println("图片：${it.absoluteFile}")
            }
            println("---- Plugin End ----, Total Time(ms) : ${System.currentTimeMillis() - start}")
        }
    }

    /**
     * 收集大图
     *
     * @param file
     * @param imageList
     */
    private fun collectBigImage(file: File, imageList: MutableList<File>) {
        if (file.isDirectory) {
            if (file.listFiles() == null) {
                println("空文件夹")
                return
            }
            file.listFiles()?.forEach {
                if (it.isDirectory) {
                    collectBigImage(it, imageList)
                } else {
                    if (isImage(it)) {
                        imageList.add(it)
                    }
                }
            }
        } else {
            if (isImage(file)) {
                imageList.add(file)
            }
        }
    }

    private fun isImage(file: File): Boolean {
        return (file.name.endsWith(".jpg") ||
                file.name.endsWith(".png") ||
                file.name.endsWith(".jpeg")
                ) && !file.name.endsWith(".9.png")
    }
}


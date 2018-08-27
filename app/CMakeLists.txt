cmake_minimum_required(VERSION 3.4.1)

#添加子目录,将会调用子目录中的CMakeLists.txt，以下add_library等内容可以分到子目录的CMakeLists.txt中
#SET(ONE_PATH ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/one)
#SET(TWO_PATH ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/two)
#MESSAGE(STATUS “src  dir: ${CMAKE_CURRENT_SOURCE_DIR}”)
#MESSAGE(STATUS “one  dir: ${ONE_PATH}”)
#MESSAGE(STATUS “two  dir: ${TWO_PATH}”)
#ADD_SUBDIRECTORY(${ONE_PATH})
#ADD_SUBDIRECTORY(${TWO_PATH})

add_library( # Specifies the name of the library.
# 这里是你so的名字。刚才在 MainActivity里面要引用的
             jniDemo

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
              #这里是刚才 创建的c++ 代码的名字
             src/main/jni/jniDemo.c )

add_library( # Specifies the name of the library.
# 这里是你so的名字。刚才在 MainActivity里面要引用的
          jniDemoCpp

          # Sets the library as a shared library.
          SHARED

          # Provides a relative path to your source file(s).
           #这里是刚才 创建的c++ 代码的名字
          src/main/jni/jniDemo.cpp )

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

target_link_libraries( # Specifies the target library.
# 这里是你so的名字。刚才在 MainActivity里面要引用的 一样。
                       jniDemo

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )

target_link_libraries( # Specifies the target library.
# 这里是你so的名字。刚才在 MainActivity里面要引用的 一样。
                    jniDemoCpp

                    # Links the target library to the log library
                    # included in the NDK.
                    ${log-lib} )

# 指定的是头文件的目录
# include_directories(src/main/jni/)
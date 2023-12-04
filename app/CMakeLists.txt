cmake_minimum_required(VERSION 3.6)

#debug and release all enable
set(CMAKE_CXX_FLAGS "-mllvm -fla -mllvm -sub -mllvm -sobf  ")

#so体积优化
#SET(CMAKE_CXX_FLAGS_DEBUG "$ENV{CXXFLAGS}   -O0 -Wall -g2 -ggdb")
#SET(CMAKE_CXX_FLAGS_RELEASE "$ENV{CXXFLAGS}  -Os -Wall -s")

#设置llvm debug模式混淆编译
SET(CMAKE_C_FLAGS_DEBUG "${CMAKE_C_FLAGS_DEBUG} -mllvm -fla -mllvm -sub -mllvm -bcf")
SET(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -mllvm -fla -mllvm -sub -mllvm -bcf")

#设置llvm release模式混淆编译
SET(CMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE} -mllvm -fla -mllvm -sub -mllvm -bcf")
SET(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -mllvm -fla -mllvm -sub -mllvm -bcf")

#set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -s -O3 -Wall -fvisibility=hidden  -mllvm -fla")

add_library( # Sets the name of the library.
        xjni

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/jni/XJni.c )

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log )

target_link_libraries( # Specifies the target library.
        xjni

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib} )
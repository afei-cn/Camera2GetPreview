cmake_minimum_required(VERSION 3.18.1)
project("nativelib")

# 指定包含的头文件路径
include_directories(
        ${CMAKE_SOURCE_DIR}/include
)

# 搜索自动目录下的所有源代码文件，并存储在 SRC_LIST 变量中
file(GLOB_RECURSE SRC_LIST ${CMAKE_SOURCE_DIR}/src/*.cpp)

add_library( native-lib
             SHARED
             ${SRC_LIST})

find_library( log-lib log )

target_link_libraries( native-lib
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib}
                       )

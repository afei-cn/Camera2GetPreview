cmake_minimum_required(VERSION 3.4.1)

add_library( native-lib
             SHARED
             ${CMAKE_SOURCE_DIR}/src/main/cpp/src/com_afei_camera2getpreview_util_NativeLibrary.cpp
             ${CMAKE_SOURCE_DIR}/src/main/cpp/src/ImageUtil.cpp )

include_directories(
    ${CMAKE_SOURCE_DIR}/src/main/cpp/include
)

find_library( log-lib
              log )

target_link_libraries( native-lib
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib}
                       )

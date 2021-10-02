cmake_minimum_required(VERSION 3.20)
project(Operating_Systems_labs)

set(CMAKE_CXX_STANDARD 14)

add_executable(Operating_Systems_labs
        Labs/Lab1/Manager/Manager.cpp Labs/Lab1/Manager/my_process.cpp Labs/Lab1/Manager/my_process.h)

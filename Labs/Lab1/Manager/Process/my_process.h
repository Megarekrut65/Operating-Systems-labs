#pragma once
#include <string>
#include <tchar.h>
#include <iostream>
#include <WS2tcpip.h>
/**
 * My process
 */
namespace mp
{
    class MyProcess {
    private:
        std::string app_path;
        PROCESS_INFORMATION pi;
        bool is_close;
        void create_new_process();
    public:
        MyProcess(const std::string &app_path);
        ~MyProcess();
        void wait_for_close();
        void close();
    };
}


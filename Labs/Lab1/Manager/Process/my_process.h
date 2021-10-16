#pragma once
#include <string>
#include <tchar.h>
#include <iostream>
#include <WS2tcpip.h>
#include "../Addition classes and functions/my_printer.h"
/**
 * My process
 */
namespace myp
{
    using other::Printer;
    /**
     * \brief Class to open some program in other process
     */
    class MyProcess {
    private:
        std::string app_path;/* Full path to program to open*/
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


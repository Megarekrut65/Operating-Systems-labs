#include "my_process.h"

namespace mp
{
    MyProcess::MyProcess(const std::string &app_path) : app_path(app_path),
    pi(PROCESS_INFORMATION()), is_close(false) {create_new_process();}

    void MyProcess::wait_for_close() {
        WaitForSingleObject(pi.hProcess, INFINITE);
        close();
    }

    void MyProcess::create_new_process() {
        STARTUPINFO si;
        ZeroMemory(&si, sizeof(si));
        si.cb = sizeof(si);
        ZeroMemory(&pi, sizeof(pi));
        if (!CreateProcess(app_path.c_str(),
                           nullptr,nullptr,nullptr,FALSE,0,nullptr,nullptr,&si,&pi))
        {
            std::cerr << "CreateProcess failed " << GetLastError() << std::endl;
            return;
        }
    }

    MyProcess::~MyProcess() {
        close();
    }

    void MyProcess::close() {
        if(is_close) return;
        is_close = true;
        CloseHandle(pi.hProcess);
        CloseHandle(pi.hThread);
        Printer::println("\nProcess (",app_path,") is closed");
    }
}
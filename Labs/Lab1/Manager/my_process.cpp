#include "my_process.h"

namespace mp
{
    MyProcess::MyProcess(const std::string &app_path, int port) : app_path(app_path),
    pi(PROCESS_INFORMATION()), port(port) {create_new_process();}

    FunctionResult MyProcess::get_result() {
        //run function using std::async
        //use socket
        WaitForSingleObject(pi.hProcess, INFINITE);
        CloseHandle(pi.hProcess);
        CloseHandle(pi.hThread);
        return 0;
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
        std::this_thread::sleep_for(std::chrono::milliseconds(port));
    }
}
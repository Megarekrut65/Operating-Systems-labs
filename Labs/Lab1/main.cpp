#include <windows.h>
#include <cstdio>
#include <tchar.h>

int main()
{
    char path[] = "C:\\Users\\User81\\source\\repos\\Megarekrut65\\Operating-Systems-labs\\Labs\\Lab1\\FirstFunction\\Debug\\FirstFunction.exe";
    char name[]= "FirstFunction";
    STARTUPINFO si;
    PROCESS_INFORMATION pi;

    ZeroMemory( &si, sizeof(si) );
    si.cb = sizeof(si);
    ZeroMemory( &pi, sizeof(pi) );

    // Start the child process.
    if( !CreateProcess( path,   // No module name (use command line)
                        name,        // Command line
                        nullptr,           // Process handle not inheritable
                        nullptr,           // Thread handle not inheritable
                        FALSE,          // Set handle inheritance to FALSE
                        0,              // No creation flags
                        nullptr,           // Use parent's environment block
                        nullptr,           // Use parent's starting directory
                        &si,            // Pointer to STARTUPINFO structure
                        &pi )           // Pointer to PROCESS_INFORMATION structure
            )
    {
        printf( "CreateProcess failed (%lu).\n", GetLastError() );
        return -1;
    }

    // Wait until child process exits.
    WaitForSingleObject( pi.hProcess, INFINITE );

    // Close process and thread handles.
    CloseHandle( pi.hProcess );
    CloseHandle( pi.hThread );
    printf("End\n");
    return 0;
}
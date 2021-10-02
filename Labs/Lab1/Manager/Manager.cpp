#include <cstdio>
#include "my_process.h"
using namespace mp;

int main()
{
    char path[] = R"(C:\Users\User81\source\repos\Megarekrut65\Operating-Systems-labs\Labs\Lab1\FirstFunction\Debug\FirstFunction.exe)";
    MyProcess process{path,2020};
    printf("%s", reinterpret_cast<const char *const>(process.get_result()));
    return 0;
}

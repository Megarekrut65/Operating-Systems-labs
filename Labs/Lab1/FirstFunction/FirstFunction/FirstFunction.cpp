#include <iostream>
#include <thread>
#include <fstream>
int main()
{
    std::ofstream file("out_f.txt");
    for(std::size_t i = 0; i < 10000000L; i++)
    {
        file << i << " ";
    }
    file << std::endl;
    file.close();
    return 0;
}
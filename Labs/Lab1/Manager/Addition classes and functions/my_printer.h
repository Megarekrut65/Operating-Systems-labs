#pragma once
#include "other_functions.h"
#include <iostream>
#include <mutex>
namespace other
{
    class Printer {
        static std::mutex mut;
        static std::ostream& out;
        template<typename T, typename ... Types>
        static void print_args(T value, Types ... args)
        {
            out << value;
            print_args(args...);
        }
        template<typename T>
        static void print_args(T value)
        {
            out << value;
        }
    public:
        template<typename T, typename ... Types>
        static void print(Color text, Color background, T value, Types ... args)
        {
            std::lock_guard<std::mutex> lock(mut);
            set_color(text,background);
            print_args(value, args...);
            set_base_colors();
        }
        template<typename T, typename ... Types>
        static void print(T value, Types ... args)
        {
            std::lock_guard<std::mutex> lock(mut);
            print_args(value, args...);
        }
        template<typename T, typename ... Types>
        static void println(T value, Types ... args)
        {
            std::lock_guard<std::mutex> lock(mut);
            print_args(value, args...);
            out << std::endl;
        }
        template<typename T, typename ... Types>
        static void println(Color text, Color background, T value, Types ... args)
        {
            std::lock_guard<std::mutex> lock(mut);
            set_color(text,background);
            print_args(value, args...);
            out << std::endl;
            set_base_colors();
        }
        static void set_base_colors()
        {
            set_color();
        }
    };
}
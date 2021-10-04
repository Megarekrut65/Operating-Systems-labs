#pragma once
#include <mutex>
namespace myshv
{
    template<typename Value>
    class SharedValue {
    private:
        Value value;
        static std::mutex mut;
    public:
        explicit SharedValue(Value value) : value(value) {}

        Value get_value() const {
            Value v;
            {
                std::lock_guard<std::mutex> lock(mut);
                v = value;
            }
            return v;
        }

        void set_value(Value value) {
            std::lock_guard<std::mutex> lock(mut);
            this->value = value;
        }
    };
    template<typename Value>
    std::mutex SharedValue<Value>::mut;
}

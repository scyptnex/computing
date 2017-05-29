#ifndef SHAREDPOINTER_H
#define SHAREDPOINTER_H

namespace week12
{
    template <class T>
    class SharedPointer
    {
    public:

        /**
         * @brief SharedPointer creates an new instance of an empty shared pointer
         */
        SharedPointer() : counter(nullptr), internal(nullptr) {}

        /**
         * @brief SharedPointer creates an new instance of a shared pointer storing item
         * @param item the item to store in a newly created const reference
         */
        SharedPointer(const T& item) : counter(new int(1)), internal(new T(item)) {}

        /**
         * @brief SharedPointer creates a new instance of a shared pointer storing the raw pointer
         * @param rawPointer the pointer to store in the newly created shared pointer
         */
        SharedPointer(T* rawPointer) : counter(new int(1)), internal(new T(*rawPointer)) {}

        /**
         * @brief SharedPointer creates a new instance of a shared pointer based on the construction of an existing shared pointers
         * @param sharedPointer the shared pointer for which the new shared pointer will be based on
         */
        SharedPointer(const SharedPointer &sharedPointer) : counter(sharedPointer.counter), internal(sharedPointer.internal) {
            *counter += 1;
        }

        /**
         * @brief ~SharedPointer destructor for the shared pointer
         */
        ~SharedPointer(){
            conditional_release();
        }

        /**
         * @brief get retrieves the raw pointer stored in the shared pointer
         * @return the raw pointer stored in the shared pointer
         */
        T* get() const {
            return internal;
        }

        /**
         * @brief reset returns the shared pointer to a state as if it had been default constructed
         */
        void reset() {
            conditional_release();
        }

        /**
         * @brief unique checks if the shared pointer object does not share ownership over its pointer with other shared_ptr objects. Empty pointers are not unique (as they do not own any pointers)
         * @return true if the shared pointer is unique otherwise false
         */
        bool unique() const{
            return counter && *counter == 1;
        }

        /**
         * @brief operator = copy assignments add the object as a shared owner of other's assets and the counter is then increased by 1
         * @param sharedPointer which is assignment to this pointer
         * @return a reference to this pointer
         */
        SharedPointer& operator=(const SharedPointer &sharedPointer) {
            if(&sharedPointer != this) {
                conditional_release();
                counter = sharedPointer.counter;
                if (counter) *counter += 1;
                internal = sharedPointer.internal;
            }
            return *this;
        }

        /**
         * @brief operator * recovers a reference to the shared pointer
         * @return a reference to the shared pointer
         */
        T& operator*() const {
            return *internal;
        }

        /**
         * @brief operator -> recovers the pointer to the object stored in the shared pointer
         * @return the pointer to the object stored in the shared pointer
         */
        T* operator->() const {
            return get();
        }

        /**
         * @brief operator bool return true iff the pointer is not null
         */
        operator bool() const {
            return counter != nullptr;
        }
    private:
        int* counter;
        T* internal;
        void conditional_release(){
            if(!*this){
                return;
            } else if(unique()){
                delete counter;
                delete internal;
            } else {
                *counter -= 1;
            }
            counter = nullptr;
            internal = nullptr;
        }
    };
}

#endif // SHAREDPOINTER_H

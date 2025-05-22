struct Message
{
    int messageType;
    String payload;
};

queue<Message> queue_;
std:: mutex mtx_;
std::condition_variable cv;

public class MessageQueue {
    void enqueue(const Message & message)
    {
        std::unique_lock<std::mutex> lock(mtx_);
        queue_.push(message);
        lock.unlock();
        cv.notify_one();
    }

    void deque()
    {
        std::unique_lock<std::mutex> lock(mtx_);
        Message msg = queue_.front();
        queue_.pop();
        return msg;
    }
}

void producer(MessageQueue& msgqueue, int msgType, String payload)
{
    Message m;
    m.messageType = msgType;
    m.payload = payload;
     msgqueue.enqueue(m);
}

void consumer(MessageQueue & msgqueue)
{
    while(true)
    {
        Message m = msgqueue.dequeue();
    }
}

int main()
{
    MessageQueue msgqueue;
    std::thread producerThread(producer, std::ref(messagequeue), 1, "message");
    std::thread consumerThread(consumer, std::ref(messagequeue));
}
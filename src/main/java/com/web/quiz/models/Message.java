package com.web.quiz.models;

public class Message {

    private MessageType type;
    private Object content;
    private Sender sender;

    public static class Sender {
        private String name;
        private String id;
        private Integer point;

        public Sender() {
        }

        public Sender(String name, String id, Integer point) {
            this.name = name;
            this.id = id;
            this.point = point;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getPoint() {
            return point;
        }

        public void setPoint(Integer point) {
            this.point = point;
        }

        @Override
        public String toString() {
            return "Sender{" +
                    "name='" + name + '\'' +
                    ", id='" + id + '\'' +
                    ", point=" + point +
                    '}';
        }
    }

    public enum MessageType {
        START, JOIN, LEAVE, END, ANSWER
    }

    public Message(MessageType type, Object content, Sender sender) {
        this.type = type;
        this.content = content;
        this.sender = sender;
    }

    public Message() {
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", content=" + content +
                ", sender=" + sender.toString() +
                '}';
    }
}

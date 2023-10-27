package com.valuewith.tweaver.chat.service;


import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.group.entity.Group;

public interface ChatRoomService {
  ChatRoom setChatRoom(Group group);
}

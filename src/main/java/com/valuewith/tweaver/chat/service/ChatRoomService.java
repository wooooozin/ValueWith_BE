package com.valuewith.tweaver.chat.service;


import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.group.entity.TripGroup;

public interface ChatRoomService {
  ChatRoom setChatRoom(TripGroup tripGroup);
}

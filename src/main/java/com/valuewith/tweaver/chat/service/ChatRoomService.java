package com.valuewith.tweaver.chat.service;

import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.chat.repository.ChatRoomRepository;
import com.valuewith.tweaver.group.entity.TripGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatRoomService {
  private final ChatRoomRepository chatRoomRepository;

  public ChatRoom createChatRoom(TripGroup tripGroup) {
    ChatRoom chatRoom = ChatRoom.builder()
        .title(tripGroup.getName())
        .tripGroup(tripGroup)
        .build();
    return chatRoomRepository.save(chatRoom);
  }
}

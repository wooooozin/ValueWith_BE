package com.valuewith.tweaver.chat.service;

import com.valuewith.tweaver.chat.ChatRoom;
import com.valuewith.tweaver.chat.repository.ChatRoomRepository;
import com.valuewith.tweaver.group.Group;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatRoomServiceImpl implements ChatRoomService{
  private final ChatRoomRepository chatRoomRepository;

  @Override
  public ChatRoom setChatRoom(Group group) {
    ChatRoom chatRoom = ChatRoom.builder()
        .title(group.getName())
        .group(group)
        .build();
    return chatRoomRepository.save(chatRoom);
  }
}

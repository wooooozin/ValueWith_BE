package com.valuewith.tweaver.chat.service;

import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.chat.repository.ChatRoomRepository;
import com.valuewith.tweaver.constants.ApprovedStatus;
import com.valuewith.tweaver.groupMember.entity.GroupMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMemberService {

  private final ChatRoomRepository chatRoomRepository;

  public String enterChatRoom(ChatRoom chatroom, GroupMember groupMember) {
    if (groupMember.getIsBanned()) {
      throw new RuntimeException("강제퇴장당해 들어갈 수 없습니다.");
    }
    if (groupMember.getApprovedStatus().equals(ApprovedStatus.PENDING)) {
      throw new RuntimeException("승인 대기중인 회원은 채팅방에 입장할 수 없습니다.");
    }
    if (groupMember.getApprovedStatus().equals(ApprovedStatus.REJECTED)) {
      throw new RuntimeException("입장할 수 없는 회원입니다.");
    }
    GroupMember newGroupMember = GroupMember.enterChatRoom(chatroom, groupMember);
    chatRoomRepository.save(newGroupMember.getChatRoom());

    return chatroom.getTitle() + " 방 참여";
  }
}

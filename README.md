##### Team Lead

**Solomon Alfa**

##### Tech Lead

**Tanmay Mathur**

##### Documentation Lead

**Yi Gong**

##### Developers

**Chiao-Yu Pai**, **Letian Zhang**, **Yunan Zhou**
#### Link
Please visit https://chatapp-final-team-rice.herokuapp.com/ to view our projects.

### Design Decisions

1. Automatically reconnect websocket when it closes due to long time no messages.

2. Put auth info in request header for backend authentication.

3. Messages from blocked users will not be visible by other members in the room.

4. If a user removes a message, it is removed from all members' message list.

5. Emoji is supported directly in text input.

6. Public Room list only displays public rooms that current user **HAS NOT** joined, while all joined rooms will be displayed in the My Room list.

7. A user joins a room by clicking on the room item in Public Room list, or accepting an invitation.

8. We use text `hate speech` to indicate hate speeches. This text will be displayed with `****` in the message panel.

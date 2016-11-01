package com.easemob.kefu.rtcmedia.protocol.types;

public enum State {
    /**
     * 用户 发送请求，等待客服接收
     */
    WAIT_KF,

    /**
     * 会议创建，客服调用 MediaService后，如果 返回正常的SID，则认为这个会议已创建
     */
    CREATED,
    /**
     * MediaService 正在邀请 双方进入会议
     */
    INVITING,
    /**
     * MediaService 邀请成功 双方振铃中
     */
    RINGING,
    /**
     * MediaService 已建立两方链接，双方正在通话
     */
    CALLING,
    /**
     * 1.一方 挂断，会议正常关闭
     * 2.一方 协商失败，主动向MediaService发送通话终止
     */
    TERMINATED,

    /**
     * 0.CREATED后，MediaService长时间处理（两种情况：1没有收到请求(如两方的AcpetC)，2service未及时响应）。被超时检查线程处理 强制关闭
     * 1.长时间等待 一方AcpetC。被超时检查线程处理 强制关闭
     * 2.任一方 协商失败
     * 3.长时间 等待 一方 应答。被超时检查线程处理 强制关闭
     * 4.通话过程中，一方 断线，造成ping无法到达，被超时检查线程处理 强制关闭
     * 5.MediaServie出现异常，主动 中断对话
     *
     */
    ABORTED
}

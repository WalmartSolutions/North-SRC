package xyz.northclient;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Random;

public class EncryptUtil {
    public String[] chinese;

    public EncryptUtil() {
        String a  ="的 一 是 了 我 不 人 在 他 有 这 个 上 们 来 到 时 大 地 为 子 中 你 说 生 国 年 着 就 那 和 要 她 出 也 得 里 后 自 以 会 家 可 下 而 过 天 去 能 对 小 多 然 于 心 学 么 之 都 好 看 起 发 当 没 成 只 如 事 把 还 用 第 样 道 想 作 种 开 美 总 从 无 情 己 面 最 女 但 现 前 些 所 同 日 手 又 行 意 动 方 期 它 头 经 \n" +
                "长 儿 回 位 分 爱 老 因 很 给 名 法 间 斯 知 世 什 两 次 使 身 者 被 高 已 亲 其 进 此 话 常 与 活 正 感 见 明 问 力 理 尔 点 文 几 定 本 公 特 做 外 孩 相 西 果 走 将 月 十 实 向 声 车 全 信 重 三 机 工 物 气 每 并 别 真 打 太 新 比 才 便 夫 再 书 部 水 像 眼 等 体 却 加 电 主 界 门 利 海 受 听 表 德 少 克 代 员  \n" +
                "许 先 口 由 死 安 写 性 马 光 白 或 住 难 望 教 命 花 结 乐 色 更 拉 东 神 记 处 让 母 父 应 直 字 场 平 报 友 关 放 至 张 认 接 告 入 笑 内 英 军 候 民 岁 往 何 度 山 觉 路 带 万 男 边 风 解 叫 任 金 快 原 吃 妈 变 通 师 立 象 数 四 失 满 战 远 格 士 音 轻 目 条 呢 病 始 达 深 完 今 提 求 清 王 化 空 业 思 切 怎  \n" +
                "非 找 片 罗 钱 吗 语 元 喜 曾 离 飞 科 言 干 流 欢 约 各 即 指 合 反 题 必 该 论 交 终 林 请 医 晚 制 球 决 传 画 保 读 运 及 则 房 早 院 量 苦 火 布 品 近 坐 产 答 星 精 视 五 连 司 巴 奇 管 类 未 朋 且 婚 台 夜 青 北 队 久 乎 越 观 落 尽 形 影 红 爸 百 令 周 吧 识 步 希 亚 术 留 市 半 热 送 兴 造 谈 容 极 随 演  \n" +
                "收 首 根 讲 整 式 取 照 办 强 石 古 华 拿 计 您 装 似 足 双 妻 尼 转 诉 米 称 丽 客 南 领 节 衣 站 黑 刻 统 断 福 城 故 历 惊 脸 选 包 紧 争 另 建 维 绝 树 系 伤 示 愿 持 千 史 谁 准 联 妇 纪 基 买 志 静 阿 诗 独 复 痛 消 社 算 义 竟 确 酒";

        chinese = a.split(" ");
    }

    public static String encrypt(String plainText, PublicKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText, PrivateKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    public static PublicKey getPublicKeyFromString(String publicKeyBase64) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(keySpec);
    }

    public static PrivateKey getPrivateKeyFromString(String privateKeyBase64) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyBase64);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return keyFactory.generatePrivate(keySpec);
    }
    public static String encrypt(String toCrypt) {
        try {
            return encrypt(toCrypt, getPublicKeyFromString("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh+uDHXiJzx4Drkr2IeD4Cq8dau8wgFpzJSlv7CmD1Qqj1TkzoaU+r3esvicJOrZQNYx51MYQ7SbT+WwGyXU1382xhozlv6oDR5QakISJDI3Y7cSBJkZLSOobrCVft082/kh8suPzAcru9KrPIi+YqzjZs2R8qqJ46gt3P4EIR7nhtRHz/rUscRWH6SIhqAmp62nmu724pAqZnAcBdNpp4hVugJzAK+0dUz8UmiDj1feBRo/zCzbXBe4oy5Sk3F/5aWcsB1TrAVrf1NqXX0oXSryHavOKx3/D9TE68ejGI/lhAVFXdgldC8f415KGRyCTz50GvX6U7JgqONTQsnu8XQIDAQAB"));
        } catch (Exception e) {

        }
        return null;
    }

    public static String decrypt(String toCrypt) {
        try {
            return decrypt(toCrypt, getPrivateKeyFromString("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCYL3yFKmlYFJZnCN6dToB5LEeCOb0g93vEBJl6bl6EwiLZY5zeES/c8CpHCmiQjJ/DysTdxGuXT5Iyr7GKdiPMiB4lin4fd4YyKRp8dKhCgP1X+RPFVoNxa71JUpoGB+hDQ+dJSQKDgruCq8cMtcir1tRDzmOMCCh3KtSWybU+Tc8MjT6fVOaQxk/QzE5HkYvIdcUv05axV4dv3DtKxBCun6vTmW7uy9mqUA3bMZO4QVJk19eQRMIpIHS/QFiYDJC+1Pjnt2FLb/ybhkf+qu5E7mEOuzAIlHLy+LWbvvBokiYmAWTFmaPhJ1vIifBmH15D8CwcUrJkrfwCMPre01Z3AgMBAAECggEAS0wjhhfH64a0K+KdkqOGuW2JLAu5YhhRKllEPulg5rrPV1QMB2vljKFfqaqitfJ+Efe/lCJdQY5jPaTkaSeu6sHmeJifcp5GzYa8zhtCLTV5JA/fEmKNwo6WJzMxdg5vguiUBRSUo9EJScwIPmerFCrE94+kfWKD8wJEER9YB6zfmkovZRaSSvO5LgVXCRVybUyXVBiN/dAKRSUEd8YWWkYAVoDFPNS3kPXxM3MUJH0WAmSpHyoejDdYucw0NBDXlcjeNGcoFBtIwk2FD+YNA7xdQnISmitmDco78utd5axch6wrkl8lBaybhgsVMITu8AAS2GsDBitTUoQR6tyN0QKBgQDaI8EjG8bHBuvu70y9ZnCQCbYk40KDV5bx9LW2suUJt7bpeGtQBW+CkdD0S/FM2/nF7n18DQPt3veXmiR1FfEdtKJmGQDWyM/TeB3lHEjG5StQWjaKWagvS/OJiWTIUfmrg9Og8XO/SXuMXbAwFnGq9Y4b/SiFx7okypNzeIva9QKBgQCymUweLByNVCbc+ZLaTHLDOj+Q4gCVG7bDEpiWJS99s2/F35GhTo7RK05W9IJD/XnJswMXUWUh3MDBF96n5k0QTSB+hfl4X6P1jmm7wP633kJKpbZsleFaDHuxQ+v4DCgR4juCD1ab9/3oYDws0ma6xHOBB9wxX05uR41TPQxgOwKBgB6AmWUbYTQ7V4pVk9l4FY1OjlWiIY7UFJtIqJAfe79cHQuQdxD9cZZEZX+Djv48VDTFIG7UMxRz9RMRzr9AjjKlK4AzRtbBxa3AQg3jYpnNOb1GoBHAb7ANYsR8nccVD2BYUqNSn+jDka8S+qv+t248X96yC5SJVRIH8Y5X8NZxAoGAMhPZy/wCZX5bB0rTanJVG3iRqkY0c5q5vCjSsAbtMgnGJW1yoBAu62eBJ9CXVgkUxQF0tG0WNlB3m6zduaZRpWh62/8k3CO1DX1JGGQdB+FCJTwh0oCUF3O4mxO4bmy6L4mHtggm+MSoZSfWt5qnQ2ciG+PgUdevXiyh690KQ1ECgYEAhknhrj3jJa0G3Xi2IDyUyUCS2UFCSH1c3iOY3ARpRi43FGYCi+wcW+FgF9EzmOGAcMmXWa1MI6rshy6RAXd+dBO1ApUn8emPY7Fg/nFiOwWYVG0bWc0y1Acih8aIwLT7UrUWoUBxMwnHnpfKzLQHqyzs7sKN0CC+H44NeX2FFcw="));
        } catch (Exception e) {

        }
        return null;
    }
}

package com.vcredit.jdev.p2p.base;

public class ResponseConstants {
	// 通用的状态码
	public abstract class CommonCode {
		// ********************system code begin*****************/
		public static final String SUCCESS_CODE = "000";// 获取数据成功状态码
		public static final String NOT_DEFINE_CODE = "001";// 没有定义错误状态码
		public static final String NO_LOGIN = "100";// 未登录状态码
		public static final String EXCEPTION_CODE = "101";// 发生异常
		// ********************system code end*****************/

		// ********************account code begin*****************/
		public static final String RESPONSE_CODE_200 = "200";// 用户不存在
		public static final String RESPONSE_CODE_201 = "201";// 用户已存在
		public static final String RESPONSE_CODE_202 = "202";// 用户名为空
		public static final String RESPONSE_CODE_203 = "203";// 密码为空
		public static final String RESPONSE_CODE_204 = "204";// 验证码为空
		public static final String RESPONSE_CODE_205 = "205";// 输入的密码不准确
		public static final String RESPONSE_CODE_206 = "206";// 保存登录记录失败
		public static final String RESPONSE_CODE_207 = "207";// 手机号为空
		public static final String RESPONSE_CODE_208 = "208";// 手机号已存在
		public static final String RESPONSE_CODE_209 = "209";// 修改手机号失败!
		public static final String RESPONSE_CODE_210 = "210";// 手机验证码为空
		public static final String RESPONSE_CODE_211 = "211";// 手机验证码验证成功
		public static final String RESPONSE_CODE_212 = "212";// 输入手机验证码不准确
		public static final String RESPONSE_CODE_213 = "213";// 发送验证码失败
		public static final String RESPONSE_CODE_214 = "214";// 图形验证码为空
		public static final String RESPONSE_CODE_215 = "215";// 图形验证码验证成功***
		public static final String RESPONSE_CODE_216 = "216";// 输入图形验证码不准确
		public static final String RESPONSE_CODE_217 = "217";// 修改Email失败
		public static final String RESPONSE_CODE_218 = "218";// 找回密码失败
		public static final String RESPONSE_CODE_219 = "219";// 当前登录密码为空
		public static final String RESPONSE_CODE_220 = "220";// 新登录密码为空
		public static final String RESPONSE_CODE_221 = "221";// 重置密码失败
		public static final String RESPONSE_CODE_222 = "222";// 安全保护问题没有选择
		public static final String RESPONSE_CODE_223 = "223";// 安全保护问题答案为空
		public static final String RESPONSE_CODE_224 = "224";// 保存安全保护问题答案失败
		public static final String RESPONSE_CODE_225 = "225";// 获取密码保护问题列表失败
		public static final String RESPONSE_CODE_226 = "226";// 账户设置了密码保护问题
		public static final String RESPONSE_CODE_227 = "227";// 账户没有设置密码保护问题
		public static final String RESPONSE_CODE_228 = "228";// 检测密保问题答案是否正确-正确
		public static final String RESPONSE_CODE_229 = "229";// 检测密保问题答案是否正确-不正确
		public static final String RESPONSE_CODE_230 = "230";// 用户注册失败*
		public static final String RESPONSE_CODE_231 = "231";// Email为空**
		public static final String RESPONSE_CODE_232 = "232";// 修改账户联系地址失败**
		public static final String RESPONSE_CODE_233 = "233";// 修改账户最高学历失败**
		public static final String RESPONSE_CODE_234 = "234";// 修改账户月收入失败**
		public static final String RESPONSE_CODE_235 = "235";// 账户职业失败**
		public static final String RESPONSE_CODE_236 = "236";// 账户检查是否设置了密码保护问题失败
		public static final String RESPONSE_CODE_237 = "237";// 账户检查是否设置了密码保护问题失败
		public static final String RESPONSE_CODE_238 = "238";// 输入手机不准确
		public static final String RESPONSE_CODE_239 = "239";// 用户名和手机号不匹配
		public static final String RESPONSE_CODE_240 = "240";// 安全等级不能为空
		public static final String RESPONSE_CODE_241 = "241";//修改联系地址所在城市失败
		public static final String RESPONSE_CODE_242 = "242";//激活的email与账户不对应
		public static final String RESPONSE_CODE_243 = "243";//发送重置密码的站内信失败
		public static final String RESPONSE_CODE_244 = "244";//手机验证码验证超时
		// ********************account code end*****************/
		/******************** deal模块 begin *************************/
		public static final String RESPONSE_CODE_300 = "300";
		public static final String RESPONSE_CODE_301 = "301";
		/******************** deal模块 end *************************/
		/******************** 商品模块 begin *************************/
		public static final String RESPONSE_CODE_599 = "599";// 检索失败
		public static final String RESPONSE_CODE_598 = "598";// 激活投资项目失败
		public static final String RESPONSE_CODE_597 = "597";// 投资项目流标失败
		public static final String RESPONSE_CODE_596 = "596";// 投资项目常规上线失败
		public static final String RESPONSE_CODE_595 = "595";// 投资项目定时上线失败
		public static final String RESPONSE_CODE_594 = "594";// 投资项目保额保息情况失败
		public static final String RESPONSE_CODE_593 = "593";// 投资项目创建失败
		public static final String RESPONSE_CODE_592 = "592";// 检索红包失败
		public static final String RESPONSE_CODE_591 = "591";// 红包未找到
		public static final String RESPONSE_CODE_590 = "590";// 红包不属于此用户
		public static final String RESPONSE_CODE_589 = "589";// 红包已兑换
		public static final String RESPONSE_CODE_588 = "588";// 红包失效
		public static final String RESPONSE_CODE_587 = "587";// 投资项目已上线
		public static final String RESPONSE_CODE_586 = "586";// 自动发布失败
		public static final String RESPONSE_CODE_585 = "585";// 投资项目强制上线失败
		public static final String RESPONSE_CODE_584 = "584";// 投资项目上线异常
		public static final String RESPONSE_CODE_583 = "583";// 投资项目不存在
		public static final String RESPONSE_CODE_582 = "582";// 投资项目不是流标状态
		public static final String RESPONSE_CODE_581 = "581";// 投资项目流标再上线失败
		public static final String RESPONSE_CODE_580 = "580";// 投资项目已经是流标再发布了
		public static final String RESPONSE_CODE_579 = "579";// 投资项目已经是流标再发布了
		public static final String RESPONSE_CODE_578 = "578";// 投资项目已存在
		/******************** 商品模块 end ***************************/

		/******************** 资金模块 begin *************************/
		public static final String RESPONSE_CODE_499 = "499";// 检索用户资金账户失败
		public static final String RESPONSE_CODE_498 = "498";// 资金账户开户失败
		public static final String RESPONSE_CODE_497 = "497";// 激活失败
		public static final String RESPONSE_CODE_496 = "496";// 分账失败
		public static final String RESPONSE_CODE_494 = "494";// 银行卡设置失败
		public static final String RESPONSE_CODE_493 = "493";// 封装数据失败
		public static final String RESPONSE_CODE_492 = "492";// 充值失败
		public static final String RESPONSE_CODE_491 = "491";// 资金账户不存在
		public static final String RESPONSE_CODE_490 = "490";// 非商户账户充值
		public static final String RESPONSE_CODE_489 = "489";// 非商户账户充值
		public static final String RESPONSE_CODE_487 = "487";// 绑卡 报错
		public static final String RESPONSE_CODE_486 = "486";// 计算手续费出错

		/******************** 资金模块 end ***************************/

		/******************** CMS(内容管理模块) begin *************************/
		public static final String RESPONSE_CODE_699 = "699";// 查找一条公告失败
		public static final String RESPONSE_CODE_698 = "698";// 查找所有公告失败
		public static final String RESPONSE_CODE_697 = "697";// 删除一条公告失败
		public static final String RESPONSE_CODE_696 = "696";// 发布消息失败

		/******************** CMS(内容管理模块) end ***************************/

		/******************** ALERT(用户消息模块) begin *************************/
		public static final String RESPONSE_CODE_799 = "799";// 查询一条用户消息失败
		public static final String RESPONSE_CODE_798 = "798";// 查询所有用户消息失败
		public static final String RESPONSE_CODE_797 = "797";// 查询一个用户所有消息失败
		public static final String RESPONSE_CODE_796 = "796";// 查询一个用户所有消息_按用户ID&日期失败
		public static final String RESPONSE_CODE_795 = "795";// 用户消息ID为空@查询一条用户消息
		public static final String RESPONSE_CODE_794 = "794";// 查找用户消息设置失败
		public static final String RESPONSE_CODE_793 = "793";// 更新一条用户消息设置失败
		public static final String RESPONSE_CODE_792 = "792";// 查询用户站内信消息总数失败
		public static final String RESPONSE_CODE_791 = "791";// 更新用户的多个消息为已读失败
		public static final String RESPONSE_CODE_790 = "790";// 删除用户的多个消息失败
		public static final String RESPONSE_CODE_789 = "789";// 删除用户的所有消息失败

		/******************** ALERT(用户消息模块) end ***************************/

		/******************** VBS同步 begin *************************/
		public static final String RESPONSE_CODE_800 = "800";// VBS数据推送全部成功
		public static final String RESPONSE_CODE_801 = "801";// VBS数据全部查询成功
		public static final String RESPONSE_CODE_802 = "802";// 未查询出符合条件的数据
		public static final String RESPONSE_CODE_803 = "803";// 查询出部分符合条件的数据
		public static final String RESPONSE_CODE_804 = "804";// 贷前查询存在返回不匹配的数据
		public static final String RESPONSE_CODE_810 = "810";// VBS数据推送部分成功
		public static final String RESPONSE_CODE_811 = "811";// 数据转换实体对象异常
		public static final String RESPONSE_CODE_812 = "812";// 不能插入重复业务编码数据
		public static final String RESPONSE_CODE_813 = "813";// 推送数据中存在不能为空的字段
		public static final String RESPONSE_CODE_814 = "814";// 对于提前清贷账单，不能处理重复业务编码数据
		public static final String RESPONSE_CODE_815 = "815";// 普通账单期数不能为空或0
		public static final String RESPONSE_CODE_816 = "816";// 该普通账单已被处理过，不需要重复推送
		public static final String RESPONSE_CODE_820 = "820";// VBS数据推送全部失败
		public static final String RESPONSE_CODE_821 = "821";// 推送数据读取错误，请检查推送数据
		public static final String RESPONSE_CODE_822 = "822";// 数据入库发生异常
		public static final String RESPONSE_CODE_830 = "830";// 系统发生未知异常
		public static final String RESPONSE_CODE_831 = "831";// 合同签约时间格式不正确
		public static final String RESPONSE_CODE_832 = "832";// 收款时间格式不正确
		public static final String RESPONSE_CODE_833 = "833";// 贷款人类型无法匹配
		public static final String RESPONSE_CODE_834 = "834";// 性别类型无法匹配
		public static final String RESPONSE_CODE_835 = "835";// 学历类型无法匹配
		public static final String RESPONSE_CODE_836 = "836";// 婚姻类型无法匹配
		public static final String RESPONSE_CODE_837 = "837";// 户籍所在地类型无法匹配
		public static final String RESPONSE_CODE_838 = "838";// 工作所在地类型无法匹配
		public static final String RESPONSE_CODE_839 = "839";// 房产情况类型无法匹配
		public static final String RESPONSE_CODE_840 = "840";// 公司行业类型无法匹配
		public static final String RESPONSE_CODE_841 = "841";// 公司规模类型无法匹配
		public static final String RESPONSE_CODE_842 = "842";// 企业性质类型无法匹配
		public static final String RESPONSE_CODE_843 = "843";// 收入类型无法匹配
		public static final String RESPONSE_CODE_844 = "844";// 年利率类型无法匹配
		public static final String RESPONSE_CODE_845 = "845";// 项目用途类型无法匹配
		public static final String RESPONSE_CODE_846 = "846";// 期数用途类型无法匹配
		public static final String RESPONSE_CODE_847 = "847";// 项目来源类型无法匹配
		public static final String RESPONSE_CODE_848 = "848";// 开户行所在省类型无法匹配
		public static final String RESPONSE_CODE_849 = "849";// 开户行所在市类型无法匹配
		public static final String RESPONSE_CODE_850 = "850";// 项目信用等级类型无法匹配
		public static final String RESPONSE_CODE_851 = "851";// 缴金情况类型无法匹配
		public static final String RESPONSE_CODE_852 = "852";// p2p已入库数据未处理
		public static final String RESPONSE_CODE_853 = "853";// p2p已入库数据处理时发生错误
		public static final String RESPONSE_CODE_854 = "854";// 房贷情况类型无法匹配

		/******************** VBS同步 end ***************************/

		/******************** RECOMMEND 用户推荐 begin *************************/
		public static final String RESPONSE_CODE_999 = "999";//查找用户推荐记录失败
		public static final String RESPONSE_CODE_998 = "998";//发送邮件推荐_用户推荐信息输入不完整
		public static final String RESPONSE_CODE_997 = "997";//发送邮件推荐失败
		public static final String RESPONSE_CODE_996 = "996";//查找用户推荐记录笔数失败
		public static final String RESPONSE_CODE_995 = "995";//查找用户推荐记录笔数失败_管理员

		/******************** RECOMMEND 用户推荐 end ***************************/

		/******************** FINANCE 金融计算 begin *************************/
		public static final String RESPONSE_CODE_A99 = "A99";//查询对比收益失败

		/******************** FINANCE 金融计算 end ***************************/
	}

	// 通用的消息
	public abstract class CommonMessage {
		// ********************system msg begin*****************/
		public static final String SUCCESS_MESSAGE = "请求数据成功!";// 获取数据失败
		public static final String NOT_DEFINE_MESSAGE = "没有定义的错误";
		public static final String NO_LOGIN_MESSAGE = "用户未登录!";// 用户未登录!
		// ********************system msg end*****************/

		// ********************account msg begin*****************/
		public static final String RESPONSE_MSG_200 = "用户不存在";
		public static final String RESPONSE_MSG_201 = "用户名已存在";
		public static final String RESPONSE_MSG_202 = "用户名为空";
		public static final String RESPONSE_MSG_203 = "密码为空";
		public static final String RESPONSE_MSG_204 = "验证码为空";
		public static final String RESPONSE_MSG_205 = "输入的密码不准确";
		public static final String RESPONSE_MSG_206 = "保存登录记录失败";
		public static final String RESPONSE_MSG_207 = "手机号为空";
		public static final String RESPONSE_MSG_208 = "手机号已存在";
		public static final String RESPONSE_MSG_209 = "修改手机号失败!";
		public static final String RESPONSE_MSG_210 = "手机验证码为空";
		public static final String RESPONSE_MSG_211 = "手机验证码验证成功";
		public static final String RESPONSE_MSG_212 = "输入手机验证码不准确";
		public static final String RESPONSE_MSG_213 = "发送验证码失败";
		public static final String RESPONSE_MSG_214 = "图形验证码为空";
		public static final String RESPONSE_MSG_215 = "图形验证码验证成功";
		public static final String RESPONSE_MSG_216 = "输入图形验证码不准确";
		public static final String RESPONSE_MSG_217 = "修改Email失败";
		public static final String RESPONSE_MSG_218 = "找回密码失败";
		public static final String RESPONSE_MSG_219 = "当前登录密码为空";
		public static final String RESPONSE_MSG_220 = "新登录密码为空";
		public static final String RESPONSE_MSG_221 = "重置密码失败";
		public static final String RESPONSE_MSG_222 = "安全保护问题没有选择";
		public static final String RESPONSE_MSG_223 = "安全保护问题答案为空";
		public static final String RESPONSE_MSG_224 = "保存安全保护问题答案失败";
		public static final String RESPONSE_MSG_225 = "获取密码保护问题列表失败";
		public static final String RESPONSE_MSG_226 = "账户设置了密码保护问题 ";
		public static final String RESPONSE_MSG_227 = "账户没有设置密码保护问题 ";
		public static final String RESPONSE_MSG_228 = "检测密保问题答案是否正确-正确";
		public static final String RESPONSE_MSG_229 = "检测密保问题答案是否正确-不正确";
		public static final String RESPONSE_MSG_230 = "用户注册失败";
		public static final String RESPONSE_MSG_231 = "Email为空";
		public static final String RESPONSE_MSG_232 = "修改账户联系地址失败";
		public static final String RESPONSE_MSG_233 = "修改账户最高学历失败";
		public static final String RESPONSE_MSG_234 = "修改账户月收入失败";
		public static final String RESPONSE_MSG_235 = "修改账户职业失败";
		public static final String RESPONSE_MSG_236 = "账户检查是否设置了密码保护问题失败";
		public static final String RESPONSE_MSG_237 = "通过用户名获取密码保护问题列表失败";
		public static final String RESPONSE_MSG_238 = "输入手机不准确";// 输入手机不准确
		public static final String RESPONSE_MSG_239 = "用户名和手机号不匹配";// 用户名和手机号不匹配
		public static final String RESPONSE_MSG_240 = "安全等级为空!";// 安全等级不能为空
		public static final String RESPONSE_MSG_241 = "修改联系地址所在城市失败";
		public static final String RESPONSE_MSG_242 = "激活的email与账户不对应";//
		public static final String RESPONSE_MSG_243 = "发送重置密码的站内信失败";//
		public static final String RESPONSE_MSG_244 = "手机验证码验证超时";//
		// ********************account msg end*****************/
		/******************** deal模块 begin *************************/
		public static final String RESPONSE_MSG_300 = "第三方支付账号未开通";
		public static final String RESPONSE_MSG_301 = "账号余额不足";
		/******************** deal模块 end *************************/
		/******************** 商品模块 begin *************************/
		public static final String RESPONSE_MSG_599 = "投资项目检索失败";// 检索失败
		public static final String RESPONSE_MSG_598 = "激活投资项目失败";// 激活投资项目失败
		public static final String RESPONSE_MSG_597 = "投资项目流标失败";// 投资项目流标失败
		public static final String RESPONSE_MSG_596 = "投资项目常规上线失败";// 投资项目常规上线失败
		public static final String RESPONSE_MSG_595 = "投资项目定时上线失败";// 投资项目常规上线失败
		public static final String RESPONSE_MSG_594 = "投资项目保额保息情况失败";// 投资项目保额保息情况失败
		public static final String RESPONSE_MSG_593 = "投资项目创建失败";// 投资项目创建失败
		public static final String RESPONSE_MSG_592 = "检索红包失败";// 检索红包失败
		public static final String RESPONSE_MSG_591 = "红包未找到";// 红包未找到
		public static final String RESPONSE_MSG_590 = "红包不属于此用户";// 红包不属于此用户
		public static final String RESPONSE_MSG_589 = "红包已兑换";// 红包已兑换
		public static final String RESPONSE_MSG_588 = "红包失效";// 红包失效
		public static final String RESPONSE_MSG_587 = "投资项目已上线";// 投资项目已上线
		public static final String RESPONSE_MSG_586 = "自动发布失败";// 自动发布失败
		public static final String RESPONSE_MSG_585 = "投资项目强制上线失败";// 投资项目强制上线失败
		public static final String RESPONSE_MSG_584 = "投资项目上线异常";// 投资项目上线异常
		public static final String RESPONSE_MSG_583 = "投资项目不存在";// 投资项目不存在
		public static final String RESPONSE_MSG_582 = "投资项目不是流标状态";// 投资项目不是流标状态
		public static final String RESPONSE_MSG_581 = "投资项目流标再上线失败";// 投资项目流标再上线失败
		public static final String RESPONSE_MSG_580 = "投资项目已经是流标再发布了";// 投资项目已经是流标再发布了
		public static final String RESPONSE_MSG_579 = "投资项目编号重复";// 投资项目已经是流标再发布了
		public static final String RESPONSE_MSG_578 = "投资项目已存在";// 投资项目已存在

		/******************** 商品模块 end ***************************/

		/******************** 资金模块 begin *************************/
		public static final String RESPONSE_MSG_499 = "检索用户资金账户失败";// 检索用户资金账户失败
		public static final String RESPONSE_MSG_498 = "资金账户开户失败";// 检索用户资金账户失败
		public static final String RESPONSE_MSG_497 = "激活失败";// 检索用户资金账户失败
		public static final String RESPONSE_MSG_496 = "分账失败";// 检索用户资金账户失败
		public static final String RESPONSE_MSG_494 = "银行卡设置失败";// 银行卡设置失败
		public static final String RESPONSE_MSG_493 = "封装数据失败";// 封装数据失败
		public static final String RESPONSE_MSG_492 = "充值失败";// 充值失败
		public static final String RESPONSE_MSG_491 = "资金账户不存在";// 资金账户不存在
		public static final String RESPONSE_MSG_490 = "非商户账户充值";// 非商户账户充值
		public static final String RESPONSE_MSG_489 = "银行卡不能为空";// 非商户账户充值
		public static final String RESPONSE_MSG_487 = "绑卡 报错";// 绑卡 报错
		public static final String RESPONSE_MSG_486 = "计算手续费出错";// 计算手续费出错

		/******************** 资金模块 end ***************************/

		/******************** CMS(内容管理模块) begin *************************/
		public static final String RESPONSE_MSG_699 = "查找一条公告失败";// 查找一条公告失败
		public static final String RESPONSE_MSG_698 = "查找所有公告失败";// 查找所有公告失败
		public static final String RESPONSE_MSG_697 = "删除一条公告失败";// 删除一条公告失败
		public static final String RESPONSE_MSG_696 = "发布消息失败";// 发布消息失败

		/******************** CMS(内容管理模块) end ***************************/

		/******************** ALERT(用户消息模块) begin *************************/
		public static final String RESPONSE_MSG_799 = "查询一条用户消息失败";
		public static final String RESPONSE_MSG_798 = "查询所有用户消息失败";
		public static final String RESPONSE_MSG_797 = "查询一个用户所有消息失败";
		public static final String RESPONSE_MSG_796 = "查询一个用户所有消息失败_按用户ID&日期";
		public static final String RESPONSE_MSG_795 = "用户消息ID为空";
		public static final String RESPONSE_MSG_794 = "查找用户消息设置失败";
		public static final String RESPONSE_MSG_793 = "更新一条用户消息设置失败";
		public static final String RESPONSE_MSG_792 = "查询用户站内信消息总数失败";
		public static final String RESPONSE_MSG_791 = "更新用户的多个消息为已读失败";
		public static final String RESPONSE_MSG_790 = "删除用户的多个消息失败";
		public static final String RESPONSE_MSG_789 = "删除用户的所有消息失败";

		/******************** ALERT(用户消息模块) end ***************************/

		/******************** VBS同步 begin *************************/
		public static final String RESPONSE_MSG_800 = "VBS数据推送全部成功";// VBS数据推送全部成功
		public static final String RESPONSE_MSG_801 = "VBS数据全部查询成功";// 
		public static final String RESPONSE_MSG_802 = "未查询出符合条件的数据";// 未查询出符合条件的数据
		public static final String RESPONSE_MSG_803 = "查询出部分符合条件的数据";// 查询出部分符合条件的数据
		public static final String RESPONSE_MSG_804 = "贷前查询存在返回不匹配的数据";// 贷前查询存在返回不匹配的数据
		public static final String RESPONSE_MSG_810 = "VBS数据推送部分成功";// VBS数据推送部分成功
		public static final String RESPONSE_MSG_811 = "数据转换实体对象异常：";// 数据转换实体对象异常
		public static final String RESPONSE_MSG_812 = "不能插入重复业务编码数据";// 不能插入重复业务编码数据
		public static final String RESPONSE_MSG_813 = "推送数据中存在不能为空的字段:";// 推送数据中存在不能为空的字段
		public static final String RESPONSE_MSG_814 = "对于提前清贷账单，不能处理重复业务编码数据";// 对于提前清贷账单，不能处理重复业务编码数据
		public static final String RESPONSE_MSG_815 = "普通账单期数不能为空或0"; //普通账单期数不能为空或0
		public static final String RESPONSE_MSG_816 = "该普通账单已被处理过，不需要重复推送";// 该普通账单已被处理过，不需要重复推送
		public static final String RESPONSE_MSG_820 = "VBS数据推送全部失败";// VBS数据推送全部失败
		public static final String RESPONSE_MSG_821 = "推送数据读取错误，请检查推送数据";// 推送数据读取错误，请检查推送数据
		public static final String RESPONSE_MSG_822 = "数据入库发生异常：";
		public static final String RESPONSE_MSG_830 = "系统发生未知异常：";// 系统发生未知异常
		public static final String RESPONSE_MSG_831 = "合同签约时间格式不正确";// 合同签约时间格式不正确
		public static final String RESPONSE_MSG_832 = "收款时间格式不正确";// 收款时间格式不正确
		public static final String RESPONSE_MSG_833 = "贷款人类型无法匹配";// 贷款人类型无法匹配
		public static final String RESPONSE_MSG_834 = "性别类型无法匹配";// 性别类型无法匹配
		public static final String RESPONSE_MSG_835 = "学历类型无法匹配";// 学历类型无法匹配
		public static final String RESPONSE_MSG_836 = "婚姻类型无法匹配";// 婚姻类型无法匹配
		public static final String RESPONSE_MSG_837 = "户籍所在地类型无法匹配";// 户籍所在地类型无法匹配
		public static final String RESPONSE_MSG_838 = "工作所在地类型无法匹配";// 工作所在地类型无法匹配
		public static final String RESPONSE_MSG_839 = "房产情况类型无法匹配";// 房产情况类型无法匹配
		public static final String RESPONSE_MSG_840 = "公司行业类型无法匹配";// 公司行业类型无法匹配
		public static final String RESPONSE_MSG_841 = "公司规模类型无法匹配";// 公司规模类型无法匹配
		public static final String RESPONSE_MSG_842 = "企业性质类型无法匹配";// 企业性质类型无法匹配
		public static final String RESPONSE_MSG_843 = "收入类型无法匹配";// 收入类型无法匹配
		public static final String RESPONSE_MSG_844 = "年利率类型无法匹配";// 年利率类型无法匹配
		public static final String RESPONSE_MSG_845 = "项目用途类型无法匹配";// 项目用途类型无法匹配
		public static final String RESPONSE_MSG_846 = "期数类型无法匹配";// 期数类型无法匹配
		public static final String RESPONSE_MSG_847 = "项目来源类型无法匹配";// 项目来源类型无法匹配
		public static final String RESPONSE_MSG_848 = "开户行所在省类型无法匹配";// 开户行所在省类型无法匹配
		public static final String RESPONSE_MSG_849 = "开户行所在市类型无法匹配";// 开户行所在市类型无法匹配
		public static final String RESPONSE_MSG_850 = "项目信用等级类型无法匹配";// 项目信用等级类型无法匹配
		public static final String RESPONSE_MSG_851 = "缴金情况类型无法匹配";// 缴金情况类型无法匹配
		public static final String RESPONSE_MSG_852 = "p2p已入库数据未处理";// p2p已入库数据未处理
		public static final String RESPONSE_MSG_853 = "p2p已入库数据处理时发生错误";// p2p已入库数据处理时发生错误
		public static final String RESPONSE_MSG_854 = "房贷情况类型无法匹配";// 房贷情况类型无法匹配
		/******************** VBS同步 end ***************************/

		/******************** RECOMMEND 用户推荐 begin *************************/
		public static final String RESPONSE_MSG_999 = "查找用户推荐记录失败";
		public static final String RESPONSE_MSG_998 = "发送邮件推荐_用户推荐信息输入不完整";
		public static final String RESPONSE_MSG_997 = "发送邮件推荐失败";
		public static final String RESPONSE_MSG_996 = "查找用户推荐记录笔数失败";
		public static final String RESPONSE_MSG_995 = "查找用户推荐记录笔数失败_管理员";

		/******************** RECOMMEND 用户推荐 end ***************************/

		/******************** FINANCE 金融计算 begin *************************/
		public static final String RESPONSE_MSG_A99 = "查询对比收益失败";

		/******************** FINANCE 金融计算 end ***************************/

	}

}

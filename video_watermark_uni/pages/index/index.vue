<template>
	<view class="content">
		<view class="platforms">
			<text class="platforms-title">Harry去水印助手</text>
			<view class="page-body">
				<view class="page-section page-section-spacing swiper">
					<swiper>
						<swiper-item>
							<view class="swiper-item">
								<view class="platforms-item">
									<image src="../../static/logo-douyin.png"></image>
									<text>抖音</text>
								</view>
								<view class="platforms-item">
									<image src="../../static/logo-gitShow.png"></image>
									<text>快手</text>
								</view>
								<view class="platforms-item">
									<image src="../../static/logo-ppx.png"></image>
									<text>皮皮虾</text>
								</view>
								<view class="platforms-item">
									<image src="../../static/logo-volcano.png"></image>
									<text>火山视频</text>
								</view>
							</view>
							<view class="swiper-item">
								<view class="platforms-item">
									<image src="../../static/logo-microview.png"></image>
									<text>微视</text>
								</view>
								<view class="platforms-item">
									<image src="../../static/logo-meipai.png"></image>
									<text>美拍</text>
								</view>
								<view class="platforms-item">
									<image src="../../static/logo-xiaokaxiu.png"></image>
									<text>小咖秀</text>
								</view>
								<view class="platforms-item">
									<image src="../../static/logo-zuiyou.png"></image>
									<text>最右</text>
								</view>
							</view>
						</swiper-item>
					</swiper>
				</view>
			</view>
		</view>

		<view class="watermark">
			<view class="watermark-input">
				<input id="inputText" placeholder=" 请复制视频链接，粘贴到这里" type="text" v-model="strUrl"></input>
				<button @tap="clear()" class="clearInputText">
					<image src="../../static/icon-clear.png" v-if="strUrl==''"></image>
					<image src="../../static/icon-clear-active.png" v-else></image>
				</button>
			</view>
			<button @click="scplayAddrs()" class="parsing" hoverClass="parsing-btn-hover">一键去除水印</button>
			<textarea class="playAddr" type="text" value="" v-model="playAddr"></textarea>
			<button @click="download()" class="parsing" hoverClass="parsing-btn-hover" v-if="playAddr.length>0">下载</button>
		</view>
	</view>
</template>

<script>
	var _self;
	export default {
		data() {
			return {
				title: '用于去除自己作品在抖音、快手、微博、美拍、微视等常见短视频平台上的视频水印',
				strUrl: '',
				playAddr: ''

			};
		},
		onLoad() {

		},
		onShow() {
			_self = this;
			// 获取粘贴板数据
			uni.getClipboardData({
				success: function(res) {
					// console.log(res.data);
					if (res.data.length > 0) {
						_self.strUrl = res.data;
					}
				}
			});
		},
		methods: {
			scplayAddrs() {
				_self = this;
				uni.request({
					url: 'http://honghh.top:8088/video/parse',
					data: {
						url: _self.strUrl
					},
					method: "POST",
					success: function(res) {
						console.log(res.data.playAddr);

						_self.playAddr = res.data.playAddr;
					},
					fail: function(err) {

					}
				});
			},
			download() {
				// console.log(this.playAddr);
				uni.downloadFile({
					url: this.playAddr, 
					success: (res) => {
						if (res.statusCode === 200) {
							console.log('下载成功');
							var tempFilePath = res.tempFilePath;
							uni.saveVideoToPhotosAlbum({
								filePath: res.tempFilePath,
								success: function(re) {
									uni.showToast({
										title: '下载成功',
										icon: 'success',
										duration: 2000
									});
									console.log('save success');
								}
							});
						}
					}
				});
			},
			clear() {
				this.strUrl = '';
				this.playAddr = '';
			}
		}
	};
</script>

<style lang="scss">
	.wrap {
		height: 100%;
	}

	.page-body {
		height: 400rpx;
		padding-top: 28rpx;
	}

	.page-section {
		position: relative;
		overflow: hidden;
		width: 514rpx;
		margin: 0 auto;
	}

	.page-section swiper {
		height: 194rpx;
	}

	.page-section_center {
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.page-section:last-child {
		margin-bottom: 0;
	}

	.page-section-gap {
		box-sizing: border-box;
		padding: 0 30rpx;
	}

	.page-section-title {
		font-size: 50rpx;
		color: #999;
		margin-bottom: 10rpx;
		padding-left: 30rpx;
		padding-right: 30rpx;
	}

	.page-section-gap .page-section-title {
		padding-left: 0;
		padding-right: 0;
	}

	.demo-text-1 {
		position: relative;
		align-items: center;
		justify-content: center;
		background-color: #1aad19;
		color: #fff;
		font-size: 36rpx;
	}

	.demo-text-1:before {
		content: 'A';
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
	}

	.demo-text-2 {
		position: relative;
		align-items: center;
		justify-content: center;
		background-color: #2782d7;
		color: #fff;
		font-size: 36rpx;
	}

	.demo-text-2:before {
		content: 'B';
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
	}

	.demo-text-3 {
		position: relative;
		align-items: center;
		justify-content: center;
		background-color: #f1f1f1;
		color: #353535;
		font-size: 36rpx;
	}

	.demo-text-3:before {
		content: 'C';
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
	}

	button {
		margin-bottom: 30rpx;
	}

	button:last-child {
		margin-bottom: 0;
	}

	.page-section-title {
		padding: 0;
	}

	.swiper-item {
		overflow: hidden;
		display: block;
		margin-bottom: 14rpx;
	}

	.page-section-title {
		margin-top: 60rpx;
		position: relative;
	}

	.info {
		position: absolute;
		right: 0;
		color: #d81818;
		font-size: 30rpx;
	}

	.page-foot {
		margin-top: 50rpx;
	}

	.platforms {
		background-color: #99CCFF;
		position: absolute;
		top: 0;
		width: 100%;
		z-index: 1;
	}

	.platforms-title {
		display: block;
		font-size: 22rpx;
		color: #999999;
		text-align: center;
		padding-top: 32rpx;
	}

	.platforms-title::before,
	.platforms-title::after {
		content: '';
		display: inline-block;
		width: 6rpx;
		height: 6rpx;
		background-color: #999999;
		border-radius: 2px;
		transform: rotate(45deg);
		vertical-align: middle;
	}

	.platforms-title::before {
		margin-right: 8rpx;
	}

	.platforms-title::after {
		margin-left: 8rpx;
	}

	.platforms-item {
		float: left;
		width: 80rpx;
		text-align: center;
		margin-right: 58rpx;
	}

	.platforms-item:last-child {
		margin-right: 0;
	}

	.platforms-item image {
		display: block;
		width: 60rpx;
		height: 60rpx;
		margin: 0 auto;
	}

	.platforms-item text {
		display: block;
		font-size: 20rpx;
		color: #fff;
		text-align: center;
		white-space: nowrap;
		margin-top: 2rpx;
	}

	.dots {
		overflow: hidden;
		width: 50rpx;
		margin: 40rpx auto 80rpx;
	}

	.dot {
		float: left;
		width: 26rpx;
		height: 8rpx;
		border-radius: 20rpx;
		margin-right: 16rpx;
		background-color: white;
	}

	.dot:last-child {
		margin-right: 0;
	}

	.active {
		width: 8rpx;
		height: 8rpx;
		background-color: #fff;
	}

	.watermark {
		position: relative;
		background-color: #fff;
		border-top-left-radius: 20rpx;
		border-top-right-radius: 20rpx;
		padding-top: 86rpx;
		top: 412rpx;
		z-index: 9;
	}

	.watermark-input {
		position: absolute;
		top: -54rpx;
		left: 50%;
		width: 664rpx;
		height: 108rpx;
		margin-left: -332rpx;
		z-index: 9;
		background-color: #fff;
		box-shadow: 0 12rpx 28rpx rgba(153, 153, 153, 0.34);
		border-radius: 16rpx;
	}

	.watermark-input input {
		float: left;
		width: 80%;
		height: 100%;
		font-size: 28rpx;
		padding-left: 30rpx;
	}

	.clearInputText {
		display: flex;
		width: 100rpx;
		height: 100%;
		border: none;
		background-color: #fff;
		align-items: center;
	}

	.clearInputText:after {
		border: none;
	}

	.clearInputText image {
		display: block;
		width: 30rpx;
		height: 30rpx;
	}

	.parsing {
		overflow: hidden;
		display: block;
		width: 664rpx;
		height: 88rpx;
		line-height: 88rpx;
		color: #fff;
		background-color: #337aff;
		box-shadow: 0 14rpx 28rpx rgba(51, 122, 255, 0.3);
	}

	.appreciate {
		color: #000;
		background-color: #f8f8f8;
		box-shadow: none;
	}

	.parsing-btn-hover {
		background: #3f6ddd;
		border: 1px solid #1e4ecb;
	}

	.list-hover-class {
		background-color: #EBEBEB;
		border-radius: 12rpx;
	}

	.faq {
		width: 664rpx;
		margin: 106rpx auto 0;
	}

	.faq-header {
		display: flex;
		justify-content: space-between;
		margin-bottom: 16rpx;
	}

	.faq-header-title {
		font-size: 28rpx;
		font-weight: 600;
	}

	.faq-header-more {
		font-size: 24rpx;
		color: #989898;
	}

	.faq-content {
		width: 100%;
		border-radius: 12rpx;
		background: #f6f6f6;
	}

	.faq-content-list {
		overflow: hidden;
		height: 108rpx;
		line-height: 108rpx;
	}

	.faq-content-list text {
		font-size: 26rpx;
		color: #666;
		margin-left: 32rpx;
	}

	.faq-content-list image {
		float: right;
		width: 16rpx;
		height: 22rpx;
		margin-top: 43rpx;
		margin-right: 38rpx;
	}

	.contact {
		font-size: 24rpx;
		text-align: center;
		padding-top: 70rpx;
		padding-bottom: 32rpx;
	}

	.contact-copy {
		color: #337aff;
		text-decoration: underline;
		margin-left: 30rpx;
	}

	.center {
		text-align: center;
		color: #f0f0f0;
	}
</style>

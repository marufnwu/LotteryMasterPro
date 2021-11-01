package com.skithub.resultdear.database

import com.skithub.resultdear.database.network.MyApi
import com.skithub.resultdear.model.*
import com.skithub.resultdear.model.response.BannerRes
import retrofit2.Response

class PdfRepositories {


    suspend fun findSimilarLotteryNumberList(pageNumber: String, itemCount: String,lotteryNumber: String, myApi: MyApi): Response<LotteryNumberResponse> {
        return myApi.findSimilarLotteryNumberList(pageNumber,itemCount,lotteryNumber)
    }

    suspend fun getLotteryNumberListUsingLotteryNumber(lotteryNumber: String, myApi: MyApi): Response<LotteryNumberResponse> {
        return myApi.getLotteryNumberListUsingLotteryNumber(lotteryNumber)
    }

    suspend fun getMiddleListUsingLotteryNumber(lotteryNumber: String, myApi: MyApi): Response<LotteryNumberResponse> {
        return myApi.getMiddleListUsingLotteryNumber(lotteryNumber)
    }

    suspend fun get2ndMiddleListUsingLotteryNumber(lotteryNumber: String, myApi: MyApi): Response<LotteryNumberResponse> {
        return myApi.get2ndMiddleListUsingLotteryNumber(lotteryNumber)
    }

    suspend fun get1stMiddleListUsingLotteryNumber(lotteryNumber: String, myApi: MyApi): Response<LotteryNumberResponse> {
        return myApi.get1stMiddleListUsingLotteryNumber(lotteryNumber)
    }

    suspend fun getLotteryNumberListByWinType(pageNumber: String, itemCount: String, winType: String, myApi: MyApi): Response<LotteryNumberResponse> {
        return myApi.getLotteryNumberListByWinType(pageNumber,itemCount,winType)
    }

    suspend fun getDuplicateLotteryNumberList(pageNumber: String, itemCount: String, myApi: MyApi): Response<LotteryNumberResponse> {
        return myApi.getDuplicateLotteryNumberList(pageNumber,itemCount)
    }

    suspend fun getMiddlePlaysMoreNumberList(pageNumber: String, itemCount: String, myApi: MyApi): Response<LotteryNumberResponse> {
        return myApi.getMiddlePlaysMoreNumberList(pageNumber,itemCount)
    }
    suspend fun gettwoNdMiddlePlaysMoreList(pageNumber: String, itemCount: String, myApi: MyApi): Response<LotteryNumberResponse> {
        return myApi.gettwoNdMiddlePlaysMoreList(pageNumber,itemCount)
    }
    suspend fun get1stNdMiddlePlaysMoreList(pageNumber: String, itemCount: String, myApi: MyApi): Response<LotteryNumberResponse> {
        return myApi.get1stNdMiddlePlaysMoreList(pageNumber,itemCount)
    }
    suspend fun getMiddleLessNumberList(pageNumber: String, itemCount: String, myApi: MyApi): Response<LotteryNumberResponse> {
        return myApi.getMiddleLessNumberList(pageNumber,itemCount)
    }

    suspend fun getLotteryNumberListByDateTime(date: String, time: String, userId: String,myApi: MyApi): Response<LotteryNumberResponse> {
        return myApi.getLotteryNumberListByDateTime(date,time,userId)
    }
    suspend fun getLotteryNumberListByDateSlot(date: String, slot: Int, userId: String,myApi: MyApi): Response<LotteryNumberResponse> {
        return myApi.getLotteryNumberListByDateSlot(date,slot,userId)
    }

    suspend fun getLotteryResultList(pageNumber: String, itemCount: String, myApi: MyApi): Response<LotteryNumberResponse> {
        return myApi.getLotteryResultList(pageNumber,itemCount)
    }

    suspend fun getBumperLotteryResultList(pageNumber: String, itemCount: String, myApi: MyApi): Response<LotteryPdfResponse> {
        return myApi.getBumperLotteryResultList(pageNumber,itemCount)
    }

    suspend fun getPaidForContact(pageId: String, userId: String, myApi: MyApi): Response<PaidForContactModel> {
        return myApi.getPaidContInfoList(pageId,userId)
    }

    suspend fun getSpeciallotteryNumber(userId: String, myApi: MyApi): Response<SpecialNumberModel> {
        return myApi.getSpecialNumberList(userId)
    }

    suspend fun getTutorialVideo(userId: String, myApi: MyApi): Response<VideoTutorResponse> {
        return myApi.getVideoList(userId)
    }

    suspend fun getAdsImageInfo(myApi: MyApi): Response<AdsImageResponse> {
        return myApi.getAdsInfo()
    }

    suspend fun getServiceInfo(pageNumber: String, myApi: MyApi): Response<ServiceInfoModel> {
        return myApi.getServiceInfo(pageNumber)
    }

    suspend fun getHomeTutorialInfo(myApi: MyApi): Response<AdsImageResponse> {
        return myApi.getHomeTutorialInfo()
    }

    suspend fun uploadUserInfo(token: String, phone: String,registrationDate: String,activeStatus: String,countryCode: String, myApi: MyApi): Response<UserInfoResponse> {
        return myApi.uploadUserInfo(token,phone,registrationDate,activeStatus,countryCode)
    }
    suspend fun getLogout(userId: String, myApi: MyApi): Response<UserInfoResponse> {
        return myApi.logouUser(userId)
    }

    suspend fun getUserInfoByToken(userId: String,token: String, myApi: MyApi): Response<UserInfoResponse> {
        return myApi.getUserInfoByToken(userId,token)
    }

    //Maruf's call

    suspend fun getBanner(banner: String, myApi: MyApi): Response<BannerRes> {
        return myApi.getBanner(banner)
    }












}
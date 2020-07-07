from django.shortcuts import render, get_object_or_404, redirect
from rest_framework.response import Response
from django.shortcuts import get_object_or_404
from rest_framework.views import APIView
from rest_framework import generics
from .models import app_User, image_post, accelometer_post, Vote_table, Vote_table_accelometer_post
from .serializers import image_postSerializer, accelometer_postSerializer, Vote_tableSerializer, app_UserSerializer, \
    Vote_table_acc_post_serializer
from rest_framework import status
from django.views.generic import (TemplateView, ListView,
                                  DetailView, CreateView,
                                  UpdateView, DeleteView)

from django.urls import reverse_lazy
from django.contrib.auth.mixins import LoginRequiredMixin
# from garbage_app.forms import PostForm, CommentForm
from django.utils import timezone
from django.contrib.auth.decorators import login_required
from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.parsers import JSONParser
from rest_framework.decorators import parser_classes
from django.core.exceptions import ObjectDoesNotExist
from django.http import JsonResponse
import logging
from rest_framework.parsers import MultiPartParser, FormParser
from math import sin, cos, sqrt, atan2
from math import radians, sin, cos, acos
import json
# from .forms import StatusForm
from django.core import serializers

# log config
logging.basicConfig(level=logging.DEBUG, format='%(asctime)s - %(levelname)s - %(message)s')


def index(request):
    return render(request, "garbage_app/home.html")


# ALL USER API'S


# list all users*****************************
class app_UserList(generics.ListCreateAPIView):
    queryset = app_User.objects.all()
    serializer_class = app_UserSerializer


# validate user API************************

@api_view(['POST'])
@parser_classes([JSONParser])
def validate_app_User_view(request):
    dict = {}
    l = []

    try:
        email_id_received = request.data[0]["email_id"]
        password_received = request.data[0]["password"]
        req_user = app_User.objects.filter(email_id=email_id_received).first()
        # if Type(req_user)==None:
        #     return Response({"message":"User Not Found"})
        actual_password = req_user.password

        if actual_password == password_received:
            serializer = app_UserSerializer(req_user)
            dict["error"] = False
            dict["message"] = "Login Successful"
            dict["user"] = serializer.data

            l.append(dict)
            # new_dict={**dict,**serializer.data}
            # return Response(dict)
            return JsonResponse(l, safe=False)

        else:
            dict["message"] = "Incorrect Password"
            dict["error"] = True
            l.append(dict)

            return JsonResponse(l, safe=False)

    except:

        dict["error"] = True
        dict["message"] = "User not found"
        l.append(dict)
        return JsonResponse(l, safe=False)


# Register app_user API  Register_Garbage_User***************

@api_view(['POST'])
def register_app_User(request):
    serializer = app_UserSerializer(data=request.data)
    dict = {}
    if serializer.is_valid():
        email = request.data["email_id"]
        # logging.debug(email)
        # logging.debug(app_User.objects.get(email_id=email))

        try:
            if app_User.objects.get(email_id=email) != None:
                dict["error"] = True
                dict["message"] = "User with same emailID already exists"
                return Response(dict)

        except app_User.DoesNotExist:
            serializer.save()
            required_user = app_User.objects.get(email_id=email)
            user_id = required_user.id
            dict["message"] = "Succesfully registered"
            dict["error"] = False
            dict["user"] = serializer.data

            return Response(dict)
    logging.debug(serializer.errors)
    logging.debug(serializer.error_messages)
    return Response({"message": "registration failed"})


# ALL POST API's

# list all image_post
class image_postList(generics.ListCreateAPIView):
    # logging.debug(request.data)
    queryset = image_post.objects.all()
    serializer_class = image_postSerializer


# list all accelometer_post
class accelometer_postList(generics.ListCreateAPIView):
    # logging.debug(request.data)
    queryset = accelometer_post.objects.all()
    serializer_class = accelometer_postSerializer


# send image_post  from android
@api_view(['POST'])
@parser_classes([MultiPartParser, FormParser])
def send_image_post(request):
    dict = {}
    # serializer=PostSerializer(data=request.data)
    # null=None
    serializer = image_postSerializer(data=request.data)

    if serializer.is_valid():
        serializer.save()
        dict["message"] = "successful"
        dict["error"] = False
        dict["Post"] = serializer.data
        logging.debug(dict)
        # queryset=Post.objects.all()
        return Response(dict)
    dict["message"] = "Unsuccessful"
    dict["error"] = True
    logging.debug(dict)
    return Response(dict)


# send accelometer_post  from android
@api_view(['POST'])
def send_accelometer_post(request):
    dict = {}
    # serializer=PostSerializer(data=request.data)
    # null=None
    serializer = accelometer_postSerializer(data=request.data)

    if serializer.is_valid():
        serializer.save()
        dict["message"] = "successful"
        dict["error"] = False
        dict["Post"] = serializer.data
        # queryset=Post.objects.all()
        logging.debug(dict)
        return Response(dict)
    dict["message"] = "Unsuccessful"
    dict["error"] = True
    logging.debug(serializer.is_valid())
    logging.debug(serializer.error_messages)
    logging.debug(serializer.errors)

    return Response(dict)


# api to delete image post delete post
@api_view(["GET"])
def delete_image_post(request, id):
    dict = {}
    try:
        entry = image_post.objects.get(id=id)
        entry.delete()
        dict["message"] = "Successfully deleted"
    except:
        dict["message"] = "Error"
        return JsonResponse(dict)
    return JsonResponse(dict, safe=False)


# api to del accelometer_post

@api_view(["GET"])
def delete_accelometer_post(request, id):
    dict = {}
    try:
        entry = accelometer_post.objects.get(id=id)
        entry.delete()
        dict["message"] = "Successfully deleted"
    except:
        dict["message"] = "Error"
        return JsonResponse(dict)
    return JsonResponse(dict, safe=False)


# API FOR VOTE**********************************

# upvote users post

@api_view(["POST"])
def upvote_image_post_view(request):
    # initialize_logger()
    dict = {}

    try:

        serializer = Vote_tableSerializer(data=request.data)
        logging.debug("Data Received")
        logging.debug(request.data["post_id"])
        received_post_id = request.data["post_id"]
        received_user_id = request.data["user_id"]
        logging.debug(received_post_id)
        post_obj = image_post.objects.get(id=received_post_id)
        logging.debug(post_obj.id)

        # if len(Vote_table.objects.filter(user_id=received_user_id).filter(post_id=received_post_id))!=0:
        #     return Response({"message":"Already liked"})

        if serializer.is_valid():
            post_obj.vote_count += 1
            post_obj.save()
            serializer.save()
            dict["error"] = False
            dict["message"] = "Upvote Successful"
            dict["send_data"] = serializer.data
            dict["updated vote count"] = post_obj.vote_count
            return Response(dict)
        else:

            return Response({"message": "Already Liked"})

    except Exception as e:
        logging.fatal(e, exc_info=True)
        dict["error"] = True
        dict["message"] = "Upvote Failed"
        dict["send_data"] = request.data
        return Response(dict)


# upvote for accelometer post is left
@api_view(["POST"])
def upvote_acc_post_view(request):
    # initialize_logger()
    dict = {}

    try:

        serializer = Vote_table_acc_post_serializer(data=request.data)
        logging.debug("Data Received")
        logging.debug(request.data["post_id"])
        received_post_id = request.data["post_id"]
        received_user_id = request.data["user_id"]
        logging.debug(received_post_id)
        post_obj = accelometer_post.objects.get(id=received_post_id)
        logging.debug(post_obj.id)

        # if len(Vote_table.objects.filter(user_id=received_user_id).filter(post_id=received_post_id))!=0:
        #     return Response({"message":"Already liked"})

        if serializer.is_valid():
            post_obj.vote_count += 1
            post_obj.save()
            serializer.save()
            dict["error"] = False
            dict["message"] = "Upvote Successful"
            dict["send_data"] = serializer.data
            dict["updated vote count"] = post_obj.vote_count
            return Response(dict)
        else:

            return Response({"message": "Already Liked"})

    except Exception as e:
        logging.fatal(e, exc_info=True)
        dict["error"] = True
        dict["message"] = "Upvote Failed"
        dict["send_data"] = request.data
        return Response(dict)


@api_view(["POST"])
def downvote_image_post(request):
    try:
        uid = request.data["user_id"]
        pid = request.data["post_id"]
        post = image_post.objects.get(id=pid)
        logging.debug(post.vote_count)
        post.vote_count -= 1
        logging.debug("check 1")
        post.save()
        logging.debug("check 2")
        entry = Vote_table.objects.filter(user_id=uid).filter(post_id=pid)
        logging.debug("check 3")

        entry.delete()
        return Response({"message": "successful", "updated_vote_count": post.vote_count})
    except:
        return Response({"message": "Failed"})


@api_view(["POST"])
def downvote_accelometer_post(request):
    try:
        uid = request.data["user_id"]
        pid = request.data["post_id"]
        post = accelometer_post.objects.get(id=pid)
        logging.debug(post.vote_count)
        post.vote_count -= 1
        logging.debug("check 1")
        post.save()
        logging.debug("check 2")
        entry = Vote_table.objects.filter(user_id=uid).filter(post_id=pid)
        logging.debug("check 3")
        entry.delete()
        return Response({"message": "successful", "updated_vote_count": post.vote_count})
    except:
        return Response({"message": "Failed"})


@api_view(["GET"])
def liked_image_post(request, uid):
    try:

        entry = Vote_table.objects.filter(user_id=uid).values()

        logging.debug(list(entry))
        # serializer=Vote_tableSerializer(data=entry,many=True)
        # if serializer.is_valid():
        return Response(list(entry))


    except:
        logging.debug("Exception")
        return Response({"message": "Failed"})


@api_view(["GET"])
def liked_accelometer_post(request, uid):
    try:

        entry = Vote_table_accelometer_post.objects.filter(user_id=uid).values()

        logging.debug(list(entry))
        # serializer=Vote_tableSerializer(data=entry,many=True)
        # if serializer.is_valid():
        return Response(list(entry))


    except:
        logging.debug("Exception")
        return Response({"message": "Failed"})


class Vote_table_list_image_post(generics.ListCreateAPIView):
    # logging.debug(request.data)
    queryset = Vote_table.objects.all()
    serializer_class = Vote_tableSerializer



class Vote_table_list_acc_post(generics.ListCreateAPIView):
    # logging.debug(request.data)
    queryset = Vote_table_accelometer_post.objects.all()
    serializer_class =Vote_table_acc_post_serializer



# get all image_post of particular user
@api_view(["GET"])
def get_users_image_post(request, uid):
    try:
        user_post = image_post.objects.filter(app_User=uid).values()
        return Response(list(user_post))
    except:
        return Response({"message": "Failed"})


# get all acc_post of particular user
@api_view(["GET"])
def get_users_accelometer_post(request, uid):
    try:
        user_post = accelometer_post.objects.filter(app_User=uid).values()
        return Response(list(user_post))
    except:
        return Response({"message": "Failed"})



@api_view(['POST'])
def filter_image_posts(request):

    try:
        logging.debug("checkbelow")
        to_send = []
        json_object=json.loads(request.body)[0]
        logging.debug(json_object)
        logging.debug("checkabove")
        logging.debug(radians(json_object["latitude"]))

        slat = radians(json_object["latitude"])
        logging.debug("dsasa")

        slon = radians(json_object["longitude"])
        radius = json_object["radius"]
        logging.debug("check3")
        all_posts = image_post.objects.all()
        i = 0

        for post in all_posts:

            logging.debug("check for in")
            elat = radians(post.latitude)
            elon = radians(post.longitude)


            #
            # if 6371.01* acos(sin(slat) * sin(elat) + cos(slat) * cos(elat) * cos(slon - elon))<=radius:
            #     serialized_obj = serializers.serialize('json', [post, ])
            #     to_send.append(serialized_obj)

            if 6371.01 * 1000 * acos(sin(slat) * sin(elat) + cos(slat) * cos(elat) * cos(slon - elon)) <= radius:
                serializer = image_postSerializer(post)
                to_send.append(serializer.data)
            logging.debug("check for end")

        # logging.debug(list(to_send))
        logging.debug(to_send)
        return Response(to_send)
    except Exception as e:
        logging.debug("chshus")
        logging.debug(str(request.data))
        return Response(to_send)



@api_view(['POST'])
def filter_accelometer_posts(request):

    try:
        to_send = []
        json_object=json.loads(request.body)[0]
        logging.debug(json_object)
        slat = radians(json_object["latitude"])
        slon = radians(json_object["longitude"])
        radius = json_object["radius"]
        all_posts = accelometer_post.objects.all()
        i = 0
        for post in all_posts:

            elat = radians(post.latitude)
            elon = radians(post.longitude)

            #
            # if 6371.01* acos(sin(slat) * sin(elat) + cos(slat) * cos(elat) * cos(slon - elon))<=radius:
            #     serialized_obj = serializers.serialize('json', [post, ])
            #     to_send.append(serialized_obj)

            if 6371.01 * 1000 * acos(sin(slat) * sin(elat) + cos(slat) * cos(elat) * cos(slon - elon)) <= radius:
                serializer = accelometer_postSerializer(post)
                to_send.append(serializer.data)

        # logging.debug(list(to_send))
        return Response(to_send)
    except Exception as e:
        logging.debug(e)
        return Response(to_send)

def index(request):
    return render(request, "pothole_app/base.html")



def image_comp(request):
    image_post_list = image_post.objects.all()
    return render(request, "pothole_app/complaints_image.html", {"image_posts":image_post_list})


def accel_comp(request):
    accel_post_list = accelometer_post.objects.all()
    return render(request, "pothole_app/complaints_accel.html", {"accel_posts":accel_post_list})


#Status API

#
# @api_view(["POST"])
# def update_image_post_status(request):
#     try:
#
#         # form=StatusForm(request.POST)
#         # status=request.data['status']
#         # uid = request.data["user_id"]
#         pid = request.data["post_id"]
#
#         status = request.data["status"]
#         post = image_post.objects.get(id=pid)
#         post.status = status
#         post.save()
#         serializer = image_postSerializer(post)
#         return render(request,"garbage_app/post_list.html",{"post_list":image_post.objects.all()})
#     except Exception as e:
#         logging.debug(e)
#         return Response({"message": "Failed"})
#

# @api_view(["POST"])
# def update_acc_post_status(request):
#     try:
#
#         # form=StatusForm(request.POST)
#         # status=request.data['status']
#         # uid = request.data["user_id"]
#         pid = request.data["post_id"]
#
#         status = request.data["status"]
#         post = accelometer_post.objects.get(id=pid)
#         post.status = status
#         post.save()
#         serializer = accelometer_postSerializer(post)
#         return render(request,"garbage_app/post_list.html",{"post_list":Post.objects.all()})
#     except Exception as e:
#         logging.debug(e)
#         return Response({"message": "Failed"})

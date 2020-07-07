from rest_framework import serializers
from .models import app_User,image_post, accelometer_post,Vote_table,Vote_table_accelometer_post


# class VoteSerializer(serializers.ModelSerializer):
#     class Meta:
#         model=Vote
#         fields="__all__"

class image_postSerializer(serializers.ModelSerializer):
    # votes=VoteSerializer(many=True,required=False)

    class Meta:
        model=image_post
        fields="__all__"

class accelometer_postSerializer(serializers.ModelSerializer):
    # votes=VoteSerializer(many=True,required=False)

    class Meta:
        model=accelometer_post
        fields="__all__"



class app_UserSerializer(serializers.ModelSerializer):
    class Meta:
        model=app_User
        fields="__all__"

class Vote_tableSerializer(serializers.ModelSerializer):

    class Meta:
        model=Vote_table
        fields="__all__"

class Vote_table_acc_post_serializer(serializers.ModelSerializer):
    class Meta:
        model=Vote_table_accelometer_post
        fields="__all__"
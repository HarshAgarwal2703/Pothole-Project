from django.urls import path
from . import views

urlpatterns = [
    path('image_complain/', views.image_comp, name="Image_Complain"),
    path('accel_complain/', views.accel_comp, name="Accel_Complain"),
]

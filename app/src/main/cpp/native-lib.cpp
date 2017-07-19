#include <jni.h>
#include <string>
#include <opencv2/core.hpp>
#include <opencv2/Imgproc.hpp>

using namespace std;
using namespace cv;

extern "C" {

    JNIEXPORT jstring JNICALL
    Java_paul_sdkslvr_MainActivity_stringFromJNI(
            JNIEnv *env,
            jobject /* this */) {
        std::string hello = "Hello from C++";
        return env->NewStringUTF(hello.c_str());
    }



    JNIEXPORT void JNICALL
    Java_paul_sdkslvr_MainActivity_writeFromCpp(
            JNIEnv *env,
            jobject /* this */,
            jlong addMat) {

        Mat &mat = *(Mat *) addMat;

        mat.setTo(Scalar(255,255,255));
        char txt[2];
        sprintf(txt, "%d", rand()%10);
        putText(mat, txt, Point(125,175), FONT_HERSHEY_SIMPLEX, 2, Scalar(0,0,0), 2);

    }
}

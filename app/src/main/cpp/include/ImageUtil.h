#ifndef FACEUNLOCK_IMAGEUTIL_H
#define FACEUNLOCK_IMAGEUTIL_H

extern "C" void i420torgba(const unsigned char *src,
                const int width,
                const int height,
                unsigned char *dst);

#endif //FACEUNLOCK_IMAGEUTIL_H

#include "ImageUtil.h"

#define MAX(a, b) ((a > b) ? a : b)
#define MIN(a, b) ((a < b) ? a : b)
#define CLAP(a) (MAX((MIN(a, 0xff)), 0x00))

void i420torgba(const unsigned char *imgY,
                const int width,
                const int height,
                unsigned char *imgDst) {
    int w, h;
    int shift = 14, offset = 8192;
    int C0 = 22987, C1 = -11698, C2 = -5636, C3 = 29049;

    int y1, y2, u1, v1;

    const unsigned char *pY1 = imgY;
    const unsigned char *pY2 = imgY + width;
    const unsigned char *pU = imgY + width * height;
    const unsigned char *pV = imgY + (int) (width * height * 1.25);

    unsigned char *pD1 = imgDst;
    unsigned char *pD2 = imgDst + width * 4;

    for (h = 0; h < height; h += 2) {
        for (w = 0; w < width; w += 2) {
            v1 = *pV - 128;
            pV++;
            u1 = *pU - 128;
            pU++;

            y1 = *pY1;
            y2 = *pY2;

            *pD1++ = CLAP(y1 + ((v1 * C0 + offset) >> shift)); // r
            *pD1++ = CLAP(y1 + ((u1 * C2 + v1 * C1 + offset) >> shift)); // g
            *pD1++ = CLAP(y1 + ((u1 * C3 + offset) >> shift)); // b
            *pD1++ = 0xff; // a
            *pD2++ = CLAP(y2 + ((v1 * C0 + offset) >> shift)); // r
            *pD2++ = CLAP(y2 + ((u1 * C2 + v1 * C1 + offset) >> shift)); // g
            *pD2++ = CLAP(y2 + ((u1 * C3 + offset) >> shift)); // b
            *pD2++ = 0xff; // a

            pY1++;
            pY2++;
            y1 = *pY1;
            y2 = *pY2;

            *pD1++ = CLAP(y1 + ((v1 * C0 + offset) >> shift)); // r
            *pD1++ = CLAP(y1 + ((u1 * C2 + v1 * C1 + offset) >> shift)); // g
            *pD1++ = CLAP(y1 + ((u1 * C3 + offset) >> shift)); // b
            *pD1++ = 0xff; // a
            *pD2++ = CLAP(y2 + ((v1 * C0 + offset) >> shift)); // r
            *pD2++ = CLAP(y2 + ((u1 * C2 + v1 * C1 + offset) >> shift)); // g
            *pD2++ = CLAP(y2 + ((u1 * C3 + offset) >> shift)); // b
            *pD2++ = 0xff; // a
            pY1++;
            pY2++;
        }
        pY1 += width;
        pY2 += width;
        pD1 += 4 * width;
        pD2 += 4 * width;
    }
}
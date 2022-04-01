# This is the File fot the Encryption Func
# Author: Lior Guzovsky, Shimom Rubin
# Date: 20/03/2022

from function import *

def door_decryption(ctext, key):
    ismsgvalid = isLenOfMsgValid(ctext)
    iskeyvalid = isLenOfKeyValid(key)
    if not ismsgvalid or not iskeyvalid:
        print('Wrong Input, terminate!')
        return
    # convert key string into matrix with values in range [0,25]
    key_matrix = createKeyMatrix(key)
    print("\nThe key marix is: ")
    print(key_matrix)
    # check if the matrix is Invertible
    if not isKeyMatrixInvertible(key_matrix):
        print("\nbad Key - the key matrix is not Invertible , terminate!")
        return
    ptxt_array = createVectorForPtxt(ctext)
    Amatpow3 = createMatPow3(key_matrix)
    AmatMul2 = matrixMulInScalar(key_matrix)
    AMatPow3PlusAMatMul2 = matrixSum(Amatpow3, AmatMul2)
    #invert AMatPow3PlusAMatMul2 matrix
    invertedMatA = invertMetrix(AMatPow3PlusAMatMul2)
    plain_txt = converToChar(calculateEncryptedPtxt(ptxt_array, invertedMatA))
    if len(plain_txt) != len(ctext):
        plain_txt.pop(len(plain_txt) - 1)
    print("\nThe plain text is: ")
    print(plain_txt)
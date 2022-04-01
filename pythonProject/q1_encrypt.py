# This is the File fot the Encryption Func
# Author: Lior Guzovsky, Shimom Rubin
# Date: 20/03/2022

from function import *


def door_encryption(ptext, key):
    ismsgvalid = isLenOfMsgValid(ptext)
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
    ptxt_array = createVectorForPtxt(ptext)
    Amatpow3 = createMatPow3(key_matrix)
    AmatMul2 = matrixMulInScalar(key_matrix)
    AMatPow3PlusAMatMul2 = matrixSum(Amatpow3, AmatMul2)
    cypher_txt = converToChar(calculateEncryptedPtxt(ptxt_array, AMatPow3PlusAMatMul2))
    if len(cypher_txt) != len(ptext):
        cypher_txt.pop(len(cypher_txt)-1)
    print("\nThe Cipher text is: ")
    print(cypher_txt)
    return cypher_txt
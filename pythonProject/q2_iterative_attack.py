# This is the File fot the Encryption Func
# Author: Lior Guzovsky, Shimom Rubin
# Date: 20/03/2022

from q1_encrypt import *
import numpy as np

def iterativeAttack(text, key):
    counter = 0
    cipher_text = text
    if(len(text)%2 == 1):
        cipher_text = cipher_text+'a'
    iteration_encrypted_text = text
    while True:
        encrypted_text = door_encryption(iteration_encrypted_text, key)
        if not np.array_equal(cipher_text, ''.join(encrypted_text)):
            prev_text = encrypted_text
            counter = counter+1
        if counter > 1 and np.array_equal(cipher_text, ''.join(encrypted_text)):
            break
        iteration_encrypted_text = encrypted_text
    if(len(text)%2 == 1):
        prev_text.pop(len(prev_text)-1)
    print('The decrypted text is: ' + str(prev_text))
    print('It took us ' + str(counter) + ' iteration to break the algorithm')

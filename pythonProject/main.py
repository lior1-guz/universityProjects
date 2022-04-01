# Autor: Lior Guzovsky, Shimom Rubin
# Date: 20/03/2022


from q1_encrypt import *
from q1_decrypt import *


# Press the green button in the gutter to run the script.
from q2_iterative_attack import iterativeAttack

if __name__ == '__main__':
    msg = input('Enter your message: ')
    key = input('\nPlease enter a Key (4 letter key): ')
    door_encryption(msg, key)
    msg = input('\nEnter Encrypted message: ')
    key = input('\nPlease enter a Key (4 letter key): ')
    door_decryption(msg, key)
    msg = input('\nEnter your message: ')
    key = input('\nPlease enter a Key (4 letter key): ')
    iterativeAttack(msg,key)
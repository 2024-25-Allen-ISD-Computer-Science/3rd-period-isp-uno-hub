import pygame
pygame.init()

clock = pygame.time.Clock()
FPS = 60

ScreenWidth = 576
ScreenHeight = 324
bgWidth = ScreenWidth
scroll = 0
run = True

Screen = pygame.display.set_mode((ScreenWidth, ScreenHeight))
pygame.display.set_caption("Parallaxing")

bg_images = []
for i in range(1, 4):
    bgImage = pygame.image.load("images/"+str(i)+".png").convert_alpha()
    bg_images.append(bgImage)

def draw_bg():
    for x in range(-2, 3):
        speed = 1
        for i in bg_images:
            Screen.blit(i, ((x * bgWidth) - (scroll * speed), 0))
            speed += 0.25

while run:

    clock.tick(FPS)

    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            run = False

    key = pygame.key.get_pressed()
    if key[pygame.K_LEFT]:
        if scroll > -3000:
            scroll -= 5
    if key[pygame.K_RIGHT]:
        if scroll < 3000:
            scroll += 5

    draw_bg()

    pygame.display.update()

pygame.quit()

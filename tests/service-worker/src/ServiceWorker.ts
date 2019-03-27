export class ServiceWorker {

    public int(): void {
        self.addEventListener('sync', (event: any) => {
            console.log('received sync event with tag <' + event.tag + '>');
            if (event.tag.startsWith('update-tile')) {
                event.waitUntil(this.updateTile(event.tag.substr('update-tile.'.length)));
            }
        });
    }

    private updateTile(tileId: string): Promise<void> {
        return (new Promise<void>((resolve, reject) => {
            console.log('update tile with id <' + tileId + '>');
            resolve();
        }));
    }
}

new ServiceWorker().int();